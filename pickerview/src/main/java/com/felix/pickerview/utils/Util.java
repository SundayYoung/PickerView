package com.felix.pickerview.utils;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class Util
{
  public static final String[] lunarNumbers = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
  public static final String[] lunarMonths = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
  public static final String[] lunarDays = { "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十", "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "廿十", "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十" };
  public static HashMap<Integer, String[]> twelveMonthWithLeapCache = new HashMap();
  
  public static int getMonthLeapByYear(int year)
  {
    return ChineseCalendar.getMonthLeapByYear(year);
  }
  
  public static int getSumOfDayInMonth(int year, int monthSway, boolean isGregorian)
  {
    if (isGregorian) {
      return getSumOfDayInMonthForGregorianByMonth(year, monthSway);
    }
    return getSumOfDayInMonthForLunarByMonthSway(year, monthSway);
  }
  
  public static int getSumOfDayInMonthForGregorianByMonth(int year, int month)
  {
    return new GregorianCalendar(year, month, 0).get(5);
  }
  
  public static int getSumOfDayInMonthForLunarByMonthSway(int year, int monthSway)
  {
    int monthLeap = ChineseCalendar.getMonthLeapByYear(year);
    int monthLunar = convertMonthSwayToMonthLunar(monthSway, monthLeap);
    return ChineseCalendar.daysInChineseMonth(year, monthLunar);
  }
  
  public static int getSumOfDayInMonthForLunarByMonthLunar(int year, int monthLunar)
  {
    return ChineseCalendar.daysInChineseMonth(year, monthLunar);
  }
  
  public static int getSumOfDayInMonthForLunarLeapYear(int year, int monthSway, int monthLeap)
  {
    int month = convertMonthSwayToMonthLunar(monthSway, monthLeap);
    return ChineseCalendar.daysInChineseMonth(year, month);
  }
  
  public static String getLunarNameOfYear(int year)
  {
    StringBuilder sb = new StringBuilder();
    int divider = 10;
    int digital = 0;
    while (year > 0)
    {
      digital = year % divider;
      sb.insert(0, lunarNumbers[digital]);
      year /= 10;
    }
    return sb.toString();
  }
  
  public static String getLunarNameOfMonth(int month)
  {
    if ((month > 0) && (month < 13)) {
      return lunarMonths[(month - 1)];
    }
    throw new IllegalArgumentException("month should be in range of [1, 12] month is " + month);
  }
  
  public static String getLunarNameOfDay(int day)
  {
    if ((day > 0) && (day < 31)) {
      return lunarDays[(day - 1)];
    }
    throw new IllegalArgumentException("day should be in range of [1, 30] day is " + day);
  }
  
  public static String[] getLunarMonthsNamesWithLeap(int monthLeap)
  {
    if (monthLeap == 0) {
      return lunarMonths;
    }
    if ((monthLeap < -12) || (monthLeap > 0)) {
      throw new IllegalArgumentException("month should be in range of [-12, 0]");
    }
    int monthLeapAbs = Math.abs(monthLeap);
    
    String[] monthsOut = (String[])twelveMonthWithLeapCache.get(Integer.valueOf(monthLeapAbs));
    if ((monthsOut != null) && (monthsOut.length == 13)) {
      return monthsOut;
    }
    monthsOut = new String[13];
    
    System.arraycopy(lunarMonths, 0, monthsOut, 0, monthLeapAbs);
    monthsOut[monthLeapAbs] = ("闰" + getLunarNameOfMonth(monthLeapAbs));
    System.arraycopy(lunarMonths, monthLeapAbs, monthsOut, monthLeapAbs + 1, lunarMonths.length - monthLeapAbs);
    
    twelveMonthWithLeapCache.put(Integer.valueOf(monthLeapAbs), monthsOut);
    return monthsOut;
  }
  
  public static int convertMonthLunarToMonthSway(int monthLunar, int monthLeap)
  {
    if (monthLeap > 0) {
      throw new IllegalArgumentException("convertChineseMonthToMonthSway monthLeap should be in range of [-12, 0]");
    }
    if (monthLeap == 0) {
      return monthLunar;
    }
    if (monthLunar == monthLeap) {
      return -monthLunar + 1;
    }
    if (monthLunar < -monthLeap + 1) {
      return monthLunar;
    }
    return monthLunar + 1;
  }
  
  public static int convertMonthSwayToMonthLunar(int monthSway, int monthLeap)
  {
    if (monthLeap > 0) {
      throw new IllegalArgumentException("convertChineseMonthToMonthSway monthLeap should be in range of [-12, 0]");
    }
    if (monthLeap == 0) {
      return monthSway;
    }
    if (monthSway == -monthLeap + 1) {
      return monthLeap;
    }
    if (monthSway < -monthLeap + 1) {
      return monthSway;
    }
    return monthSway - 1;
  }
  
  public static int convertMonthSwayToMonthLunarByYear(int monthSway, int year)
  {
    int monthLeap = getMonthLeapByYear(year);
    return convertMonthSwayToMonthLunar(monthSway, monthLeap);
  }
  
  public static String getJson(Context context, String fileName)
  {
    StringBuilder stringBuilder = new StringBuilder();
    try
    {
      AssetManager assetManager = context.getAssets();
      
      BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
      String line;
      while ((line = bf.readLine()) != null) {
        stringBuilder.append(line);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return stringBuilder.toString();
  }
}
