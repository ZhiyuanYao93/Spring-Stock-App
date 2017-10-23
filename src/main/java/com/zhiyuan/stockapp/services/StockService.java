package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.models.Stock;

/**
 * Created by Zhiyuan Yao
 */
public interface StockService {
    Stock saveStock(Stock stock);
    Stock updateStock(Stock stock);

    Stock findById(Integer stockId);
    Stock findByName(String stockName);
    Stock findBySymbol(String stockSymbol);

    void deleteById(Integer stockId);
    void deleteByName(String stockName);
    void deleteBySymbol(String stockSymbol);
}
