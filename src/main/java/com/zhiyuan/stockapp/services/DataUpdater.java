package com.zhiyuan.stockapp.services;

import com.zhiyuan.stockapp.models.User;

/**
 * Created by Zhiyuan Yao
 */
public interface DataUpdater {
    Boolean updateStockInDB(User user);
}
