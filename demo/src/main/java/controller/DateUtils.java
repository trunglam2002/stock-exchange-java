package controller;

import java.sql.Date;
import java.time.LocalDate;

public class DateUtils {
    public static Date getCurrentDate() {
        return Date.valueOf(LocalDate.now());
    }
}