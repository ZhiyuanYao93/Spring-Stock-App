package com.zhiyuan.stockapp.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
@Getter
@Setter

@Component
public class StockUserFusion {

    private String stockSymbol;

    private Integer userId;
    private String userName;
    private String password;
    private List<Stock> stocks = new ArrayList<>();

    public StockUserFusion() {

    }

    public StockUserFusion(Integer userId, String userName, String password, List<Stock> stocks) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.stocks = stocks;
    }
}
