package com.algotrading;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String yyyy_mm_ddToYYYYMMDD(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String yyyymmddToYYYY_MM_DD(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

}
