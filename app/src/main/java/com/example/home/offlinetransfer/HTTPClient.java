package com.example.home.offlinetransfer;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Home on 10-05-2017.
 */
public class HTTPClient {

    public static String post(String surl,String params){
        String result="";

        HttpURLConnection connection = null;


        URL url = null;
        try {
            url = new URL(surl + "?" + params);

            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            char c;
            while ((c = (char) input.read()) != (char) -1)
                result += c;
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
}
