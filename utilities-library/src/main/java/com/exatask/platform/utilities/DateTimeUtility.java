package com.exatask.platform.utilities;

import com.exatask.platform.utilities.constants.DateTimeConstant;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Map;

@UtilityClass
public class DateTimeUtility {

  private static final Map<DateTimeConstant.Format, DateTimeFormatter> DATE_TIME_FORMATS = new EnumMap<>(DateTimeConstant.Format.class);

  public static final String DATE = "yyyy-MM-dd";
  public static final String TIME = "HH:mm:ss";
  public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
  public static final String ISO_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";

  static {
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.SIMPLE_DATE, DateTimeFormatter.ofPattern(DATE));
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.SIMPLE_TIME, DateTimeFormatter.ofPattern(TIME));
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.SIMPLE_DATE_TIME, DateTimeFormatter.ofPattern(DATE_TIME));
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.ISO_DATE, DateTimeFormatter.ISO_DATE);
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.ISO_TIME, DateTimeFormatter.ISO_TIME);
    DATE_TIME_FORMATS.put(DateTimeConstant.Format.ISO_DATE_TIME, DateTimeFormatter.ISO_DATE_TIME);
  }

  public static String toString(LocalDateTime dateTime, DateTimeConstant.Format format) {
    return dateTime.format(DATE_TIME_FORMATS.get(format));
  }

  public static String toString(LocalDate date, DateTimeConstant.Format format) {
    return date.format(DATE_TIME_FORMATS.get(format));
  }

  public static String toUtcString(LocalDateTime dateTime, DateTimeConstant.Format format) {

    return dateTime.atZone(ZoneId.of(ZoneOffset.UTC.getId()))
        .format(DATE_TIME_FORMATS.get(format));
  }

  public static LocalDateTime toDateTime(String dateTime, DateTimeConstant.Format format) {
    return LocalDateTime.parse(dateTime, DATE_TIME_FORMATS.get(format));
  }

  public static LocalDate toDate(String date, DateTimeConstant.Format format) {
    return LocalDate.from(DATE_TIME_FORMATS.get(format).parse(date));
  }
}
