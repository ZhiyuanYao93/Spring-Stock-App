package com.zhiyuan.stockapp.repository;

import com.zhiyuan.stockapp.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Zhiyuan Yao
 */

public interface StockRepository extends JpaRepository<Stock,Integer>{
    Optional<Stock> findByStockId(Integer stockId);
    Optional<Stock> findByStockName(String sotckName);
    Optional<Stock> findByStockSymbol(String stockSymbol);

    void deleteByStockSymbol(String stockSymbol);
    void deleteByStockName(String stockName);

}
