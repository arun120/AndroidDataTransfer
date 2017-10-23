package com.example.home.offlinetransfer;

/**
 * Created by Home on 05-05-2017.
 */
public class Encoder {

    public static  String encode(String payee,String amt,String type){
        return payee+"*"+amt+"*"+type;
    }

}
