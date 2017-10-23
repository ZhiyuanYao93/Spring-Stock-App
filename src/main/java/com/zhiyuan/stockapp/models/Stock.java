package com.zhiyuan.stockapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Entity
@Data
@EqualsAndHashCode(exclude = {"users"})
@NoArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stockId;

    private String stockName;
    private String stockSymbol;
    private Double stockBidPrice;
    private Double StockAskPrice;


    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "stocks")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", stockName='" + stockName + '\'' +
                ", stockSymbol='" + stockSymbol + '\'' +
                ", stockBidPrice=" + stockBidPrice +
                ", StockAskPrice=" + StockAskPrice +
                '}';
    }
}
