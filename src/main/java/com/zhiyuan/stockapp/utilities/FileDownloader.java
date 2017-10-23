package com.zhiyuan.stockapp.utilities;

import com.zhiyuan.stockapp.Exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhiyuan Yao
 */
@Slf4j
public class FileDownloader {
    public static void main(String[] args) throws IOException {
//        String[] symbols = {"AAPL","GOOG","MSFT"};

        String[] symbols = {"OOOOOOOO"};

        FileDownloader.getDataFromURL(symbols);


    }

    public static List<String[]> getDataFromURL(String[] symbols) throws IOException {

         List<String[]> stockData = new ArrayList<>(symbols.length);

        //Sample URL: http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+MSFT&f=nab
        //nab ==> name + ask + bid

        final String BASE_YAHOO_URL = "http://download.finance.yahoo.com/d/quotes.csv?s=";

        StringBuilder stringBuilder = new StringBuilder(BASE_YAHOO_URL);

        for (String symbol : symbols){
            stringBuilder.append(symbol);
            stringBuilder.append("+");
        }

        stringBuilder.append("&f=nabs");



        URL url = new URL(stringBuilder.toString());

        URLConnection connection = url.openConnection();

        InputStreamReader input = new InputStreamReader(connection.getInputStream());
        BufferedReader buffer = null;
        String line = "";
        String csvSplitBy = ",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)";

        try {

            buffer = new BufferedReader(input);
            while ((line = buffer.readLine()) != null) {
                String[] singleStock = line.split(csvSplitBy,-1);
                System.out.println(singleStock.length);
                log.info("Stock [name =" + singleStock[0] + " , Ask =" + singleStock[1] + " , Bid =" + singleStock[2] + " , Symbol= " + singleStock[3] );
                if (!singleStock[0].equals("N/A")){
                    stockData.add(singleStock);
                }else{
                    throw new NotFoundException("No Stock with this symbol " + singleStock[3] + " found");
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        log.debug(stockData.toString());
        return stockData;
    }
}
