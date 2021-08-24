package com.exatask.platform.utilities;

import com.exatask.platform.utilities.constants.DateTime;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DateTimeUtility {

  private static final Map<DateTime.Format, SimpleDateFormat> dateTimeFormats = new HashMap<>();

  static {
    dateTimeFormats.put(DateTime.Format.SIMPLE_DATE, new SimpleDateFormat("yyyy-MM-dd"));
    dateTimeFormats.put(DateTime.Format.SIMPLE_TIME, new SimpleDateFormat("HH:mm:ss"));
    dateTimeFormats.put(DateTime.Format.SIMPLE_DATE_TIME, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
  }

  public static String toString(Date date, DateTime.Format format) {

    SimpleDateFormat dateTimeFormat = dateTimeFormats.get(format);
    if (dateTimeFormat == null) {
      return null;
    }

    return dateTimeFormat.format(date);
  }

  public static Date toDate(String date, DateTime.Format format) throws ParseException {

    SimpleDateFormat dateTimeFormat = dateTimeFormats.get(format);
    if (dateTimeFormat == null) {
      return null;
    }

    return dateTimeFormat.parse(date);
  }
}
