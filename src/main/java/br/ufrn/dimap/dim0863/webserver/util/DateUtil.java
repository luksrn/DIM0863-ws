package br.ufrn.dimap.dim0863.webserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static synchronized String convertToString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static synchronized Date convertFromString(String date) {
        try {
            return DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            //TODO Change
            return new Date();
        }
    }

}