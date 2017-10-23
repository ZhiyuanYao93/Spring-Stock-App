package com.zhiyuan.stockapp.controllers;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import com.zhiyuan.stockapp.models.Stock;
import com.zhiyuan.stockapp.models.StockUserFusion;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.services.StockService;
import com.zhiyuan.stockapp.services.UserService;
import com.zhiyuan.stockapp.utilities.SymbolValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Zhiyuan Yao
 */

@Slf4j
@Controller
public class StockController {
    private static final String STOCK_NEWSTOCKFORM_URL = "stock/stockform";
    private final StockService stockService;
    private final UserService userService;

    public StockController(StockService stockService, UserService userService) {
        this.stockService = stockService;
        this.userService = userService;
    }



    @GetMapping("/user/{id}/stock/new")
    public String redirectUserToStockForm(@PathVariable(value = "id") int userId, Model model){
        User user = userService.findUserById(userId);

        StockUserFusion stockUserFusion = new StockUserFusion(user.getUserId(),user.getUserName(),user.getPassword(),user.getStocks());
        model.addAttribute("user",stockUserFusion);

        log.debug("Stock_From included user: " + user.toString());

        return STOCK_NEWSTOCKFORM_URL;
    }


    @PostMapping
    @RequestMapping("stock")
    public String saveStock(@ModelAttribute("user") StockUserFusion stockUserFusion, BindingResult bindingResult){
    //1.validate symbol. If illegal, go back to STOCK_FORM_URL
        //TODO:do not waste this validation. validation connects and bring back data.
        log.debug("Validating stock symbol....");
        if (!SymbolValidator.validateSymbol(stockUserFusion.getStockSymbol())){
            log.debug("Illegal symbol, redirecting to StockForm...");
            return "redirect:/user/" + stockUserFusion.getUserId() + "/stock/new";
        }

    //2.assign data to User and Stock.
        User userToAddStock = new User();
        userToAddStock.setUserId(stockUserFusion.getUserId());
        userToAddStock.setUserName(stockUserFusion.getUserName());
        userToAddStock.setPassword(stockUserFusion.getPassword());
        userToAddStock.setStocks(stockUserFusion.getStocks());


        Stock stockToAddUser = new Stock();
        stockToAddUser.setStockSymbol(stockUserFusion.getStockSymbol());
        log.info("stockSymbol is : " + stockUserFusion.getStockSymbol());

        Stock stockInDB = new Stock();
        try{
            stockInDB = stockService.findBySymbol(stockToAddUser.getStockSymbol());
        }catch(NotFoundException nfe){
            log.debug("Stock is not found in DB. Saving it.");
            stockInDB = stockService.saveStock(stockToAddUser);
        }

        if(bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError -> {
                log.error(objectError.toString());
            });
            return "redirect:/stock/" + userToAddStock.getUserId() + "/new";

        }

        log.debug("Stock form passed in : " + stockToAddUser.toString());
        log.debug("Stock form passed in : " + userToAddStock.toString());

    //3.Now both the User and Stock are in DB.
    //  Update user and stock.
    //  if Stock is in DB, 1) add Stock to user and 2) add User to Stock
    //
    //

        Boolean userHasStock = false;

        for (Stock stock : userToAddStock.getStocks()){
            if (stock.getStockSymbol().equals(stockInDB.getStockSymbol())){
                userHasStock = true;
            }
        }

        Boolean stockHasUser = false;

        for (User user : stockInDB.getUsers()){
            if (user.getUserName().equals(userToAddStock.getUserName())){
                stockHasUser = true;
            }
        }

        if (!userHasStock){
            userToAddStock.getStocks().add(stockInDB);
        }

        if (!stockHasUser){
            stockInDB.getUsers().add(userToAddStock);

        }


        //TODO: Junction table will grow fast because of the updated User's ID. Old record can still exists.
        //Thought: delete old record after retriving it.
        //userService.deleteUserById(userToAddStock.getUserId());
        Stock updatedStock = stockService.updateStock(stockInDB);
        User updatedUser = userService.updateUser(userToAddStock);

        log.debug("UpdatedUser is: " + updatedUser.toString());
        log.debug("UpdatedStock is: " + updatedStock.toString());


        return "redirect:/user/" +updatedUser.getUserId() +"/home";

    }

    @RequestMapping("/user/{userId}/stock/{stockId}/delete")
    public String deleteStock(@PathVariable(value = "userId") Integer userId, @PathVariable(value = "stockId") Integer stockId){
        User userToEdit = userService.findUserById(userId);
        Stock stockToDelete = stockService.findById(stockId);

        userToEdit.getStocks().remove(stockToDelete);
        stockToDelete.getUsers().remove(userToEdit);

        //TODO: Junction table will grow fast because of the updated User's ID. Old record can still exists.
        //Solution: delete old record after retriving it.
//        userService.deleteUserById(userToEdit.getUserId());

        User updatedUser = userService.updateUser(userToEdit);
        Stock updatedStock = stockService.updateStock(stockToDelete);

        return "redirect:/user/"+ updatedUser.getUserId() +"/home";
    }



}
