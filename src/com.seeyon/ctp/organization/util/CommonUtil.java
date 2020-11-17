package com.seeyon.ctp.organization.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CommonUtil {
    public static String generateID() throws Exception {
        String key = null;
        try {
            key = formatDateToString(new Date(), "yyyyMMddHHmmss") + generateNumberString(4);
            return key;
        } catch (Exception var2) {
            throw var2;
        }
    }

    public static String formatDateToString(Date date, String model) {
        SimpleDateFormat dateformat = new SimpleDateFormat(model);
        String strDate = dateformat.format(date);
        return strDate;
    }

    public static String generateNumberString(int length) throws Exception {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();

        try {
            for(int i = 0; i < length; ++i) {
                sb.append("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(random.nextInt("0123456789".length())));
            }
        } catch (Exception var4) {
            throw var4;
        }

        return sb.toString();
    }


    public static String isJudgeNull(String str) throws Exception {
        String result="";
        if(null!=str ){
            result=str;
        }
        return result;
    }
}
