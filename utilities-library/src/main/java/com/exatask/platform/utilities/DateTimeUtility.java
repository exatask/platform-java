package com.exatask.platform.utilities;

import com.exatask.platform.utilities.constants.DateTimeConstant;
import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class DateTimeUtility {

  private static final Map<DateTimeConstant.Format, DateTimeFormatter> DATE_TIME_FORMATS = new HashMap<>();

  static {
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.SIMPLE_DATE, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.SIMPLE_TIME, DateTimeFormatter.ofPattern("HH:mm:ss"));
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.SIMPLE_DATE_TIME, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.ISO_DATE, DateTimeFormatter.ISO_DATE);
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.ISO_TIME, DateTimeFormatter.ISO_TIME);
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.ISO_DATE_TIME, DateTimeFormatter.ISO_DATE_TIME);
  }

  public static String toString(LocalDateTime date, DateTimeConstant.Format format) {
    return date.format(DATE_TIME_FORMATS.get(format));
  }

  public static LocalDateTime toDate(String date, DateTimeConstant.Format format) throws ParseException {
    return LocalDateTime.from(DATE_TIME_FORMATS.get(format).parse(date));
  }
}
