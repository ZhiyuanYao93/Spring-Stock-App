package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.models.Stock;
import com.zhiyuan.stockapp.models.User;
import com.zhiyuan.stockapp.repository.StockRepository;
import com.zhiyuan.stockapp.repository.UserRepository;
import com.zhiyuan.stockapp.utilities.FileDownloader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Service
public class DataUpdaterImpl implements DataUpdater {
    private final UserService userService;
    private final StockService stockService;

    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public DataUpdaterImpl(UserService userService, StockService stockService, UserRepository userRepository, StockRepository stockRepository) {
        this.userService = userService;
        this.stockService = stockService;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Override
    public Boolean updateStockInDB(User user) {
        if (user.getStocks().size() == 0 || user.getStocks() == null){
            return true;
        }
        //get all the stocks that this user has
        List<Stock> stockLists = user.getStocks();

        //generate a string array of symbols and pass to FileDownloader
        List<String> stockSymbolList = new ArrayList<>(stockLists.size());

        for (Stock stock : stockLists){
            stockSymbolList.add(stock.getStockSymbol());
            log.info("Added " + stock.getStockSymbol() + "to List for fetching data");
        }

        String[] stockSymbolArray = stockSymbolList.toArray(new String[stockLists.size()]);

        //TODO:Handle exception in a better way.

        List<String[]> fetchedData = new ArrayList<>(stockSymbolArray.length);


        try {
            fetchedData = FileDownloader.getDataFromURL(stockSymbolArray);
        } catch (IOException e) {
            return false;
        }

        //fetchedData's String[] format is [name,ask,bid,symbol]
        //assign the new Bid and Ask prices to the refrerences and save.

        for (String[] singleStockData : fetchedData){
            //retrieve
            Stock stock = stockService.findBySymbol(singleStockData[3].replace("\"",""));
            stock = stockRepository.findOne(stock.getStockId());
            //assign
            if (singleStockData[1].replace("\"","").equals("N/A")){
                singleStockData[1] = "0.0";
            }

            if (singleStockData[2].replace("\"","").equals("N/A")){
                singleStockData[2] = "0.0";
            }
            stock.setStockName(singleStockData[0].replace("\"",""));
            stock.setStockAskPrice(Double.valueOf(singleStockData[1]));
            stock.setStockBidPrice(Double.valueOf(singleStockData[2]));
            //save
            Stock updatedStock = stockRepository.save(stock);
        }

        return true;
    }
}
