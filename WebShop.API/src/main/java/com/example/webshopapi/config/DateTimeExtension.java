package com.example.webshopapi.config;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DateTimeExtension
{
    public static LocalDateTime AddBusinessDays(LocalDateTime date, int days)
    {
        int sign = Integer.signum(days);
        int unsignedDays = Math.abs(days);
        for (int i = 0; i < unsignedDays; i++)
        {
            do
            {
                date = date.plusDays(sign);
            } while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
        }
        return date;
    }
}
