package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import com.zhiyuan.stockapp.models.Stock;
import com.zhiyuan.stockapp.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Zhiyuan Yao
 */
@Service
@Slf4j
public class StockServiceImpl  implements StockService{

    private StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    @Override
    public Stock saveStock(Stock stock) {
        Stock savedStock = new Stock();
        savedStock = stockRepository.save(stock);
        return savedStock;
    }

    @Override
    public Stock updateStock(Stock stock) {
        Stock stockReference = stockRepository.findOne(stock.getStockId());
        stockReference.setUsers(stock.getUsers());
        return stockRepository.save(stockReference);
    }

    @Override
    public Stock findByName(String stockName) {
        Optional<Stock> stockOptional = stockRepository.findByStockName(stockName);
        if (!stockOptional.isPresent()){
            log.error("Stock with name {} is not found.", stockName);
            throw new NotFoundException("Stock with name: " + stockName + " is not found");
        }
        return stockOptional.get();
    }

    @Override
    public Stock findBySymbol(String stockSymbol) {
        Optional<Stock> stockOptional = stockRepository.findByStockSymbol(stockSymbol);
        if (!stockOptional.isPresent()){
            log.error("Stock with symbol {} is not found.", stockSymbol);
            throw new NotFoundException("Stock with symbol: " + stockSymbol + " is not found");
        }
        return stockOptional.get();
    }

    @Override
    public Stock findById(Integer stockId) {
        Optional<Stock> stockOptional = stockRepository.findByStockId(stockId);
        if (!stockOptional.isPresent()){
            log.error("Stock with ID {} is not found.", stockId);
            throw new NotFoundException("Stock with ID: " + stockId + " is not found");
        }
        return stockOptional.get();
    }

    @Override
    public void deleteById(Integer stockId) {
        stockRepository.delete(stockId);
    }

    @Override
    public void deleteByName(String stockName) {
        stockRepository.deleteByStockName(stockName);
    }

    @Override
    public void deleteBySymbol(String stockSymbol) {
        stockRepository.deleteByStockSymbol(stockSymbol);
    }
}
