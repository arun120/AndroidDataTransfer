package com.example.home.offlinetransfer;

/**
 * Created by Home on 05-05-2017.
 */
public class Encoder {

    public static  String encode(String amt,String num,String IFSC,String ack){
        return amt+"*"+num+"*"+IFSC+"*"+ack;
    }

}
