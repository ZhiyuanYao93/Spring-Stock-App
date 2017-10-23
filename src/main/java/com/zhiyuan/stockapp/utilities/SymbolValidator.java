package com.zhiyuan.stockapp.utilities;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
public class SymbolValidator {
    public static void main(String[] args) {
        String symbol = "GOOG";

       SymbolValidator.validateSymbol(symbol);


    }

    public static Boolean validateSymbol(String symbol){
        String[] symbols = {symbol};
        List<String[]> fetchResult = new ArrayList<>();


        try {
            fetchResult = FileDownloader.getDataFromURL(symbols);
        } catch (NotFoundException nfe) {
            log.error("!!!! In SymboValidator. Symbol" + symbol +" NOT FOUND from URL!!!!");
           return false;
        }catch (IOException IOE){
            IOE.printStackTrace();
        }

        log.debug("In SymboValidator. Symbol " + symbol +" found from URL");

       return true;
    }
}
