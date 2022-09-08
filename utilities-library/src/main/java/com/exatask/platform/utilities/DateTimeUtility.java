package com.exatask.platform.utilities;

import com.exatask.platform.utilities.constants.DateTimeConstant;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DateTimeUtility {

  private static final Map<DateTimeConstant.Format, SimpleDateFormat> dateTimeFormats = new HashMap<>();

  static {
    dateTimeFormats.put(DateTimeConstant.Format.SIMPLE_DATE, new SimpleDateFormat("yyyy-MM-dd"));
    dateTimeFormats.put(DateTimeConstant.Format.SIMPLE_TIME, new SimpleDateFormat("HH:mm:ss"));
    dateTimeFormats.put(DateTimeConstant.Format.SIMPLE_DATE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
  }

  public static String toString(Date date, DateTimeConstant.Format format) {

    SimpleDateFormat dateTimeFormat = dateTimeFormats.get(format);
    if (dateTimeFormat == null) {
      return null;
    }

    return dateTimeFormat.format(date);
  }

  public static Date toDate(String date, DateTimeConstant.Format format) throws ParseException {

    SimpleDateFormat dateTimeFormat = dateTimeFormats.get(format);
    if (dateTimeFormat == null) {
      return null;
    }

    return dateTimeFormat.parse(date);
  }
}
