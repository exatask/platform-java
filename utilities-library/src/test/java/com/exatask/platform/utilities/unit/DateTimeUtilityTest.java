package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.DateTimeUtility;
import com.exatask.platform.utilities.constants.DateTimeConstant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class DateTimeUtilityTest {

  @ParameterizedTest
  @MethodSource("shouldReturnDateTimeStringParameters")
  public void shouldReturnDateTimeString_toString(LocalDateTime dateTime, DateTimeConstant.Format format, String dateTimeString) {

    Assertions.assertEquals(dateTimeString, DateTimeUtility.toString(dateTime, format));
  }

  public static Stream<Arguments> shouldReturnDateTimeStringParameters() {

    LocalDateTime localDateTime = ZonedDateTime.of(2024, 3, 16, 20, 15, 30, 0, ZoneId.of("Asia/Kolkata"))
        .toLocalDateTime();

    return Stream.of(
        Arguments.of(localDateTime, DateTimeConstant.Format.SIMPLE_DATE, "2024-03-16"),
        Arguments.of(localDateTime, DateTimeConstant.Format.SIMPLE_TIME, "20:15:30"),
        Arguments.of(localDateTime, DateTimeConstant.Format.SIMPLE_DATE_TIME, "2024-03-16 20:15:30"),
        Arguments.of(localDateTime, DateTimeConstant.Format.ISO_DATE, "2024-03-16"),
        Arguments.of(localDateTime, DateTimeConstant.Format.ISO_TIME, "20:15:30"),
        Arguments.of(localDateTime, DateTimeConstant.Format.ISO_DATE_TIME, "2024-03-16T20:15:30")
    );
  }

  @ParameterizedTest
  @MethodSource("shouldReturnDateStringParameters")
  public void shouldReturnDateString_toString(LocalDate date, DateTimeConstant.Format format, String dateString) {

    Assertions.assertEquals(dateString, DateTimeUtility.toString(date, format));
  }

  public static Stream<Arguments> shouldReturnDateStringParameters() {

    LocalDate localDate = ZonedDateTime.of(2024, 3, 16, 20, 15, 30, 0, ZoneId.of("Asia/Kolkata"))
        .toLocalDate();

    return Stream.of(
        Arguments.of(localDate, DateTimeConstant.Format.SIMPLE_DATE, "2024-03-16"),
        Arguments.of(localDate, DateTimeConstant.Format.ISO_DATE, "2024-03-16")
    );
  }

  @ParameterizedTest
  @MethodSource("shouldReturnUtcDateTimeStringParameters")
  public void shouldReturnUtcDateTimeString_toUtcString(LocalDateTime dateTime, DateTimeConstant.Format format, String dateTimeString) {

    Assertions.assertEquals(dateTimeString, DateTimeUtility.toUtcString(dateTime, format));
  }

  public static Stream<Arguments> shouldReturnUtcDateTimeStringParameters() {

    LocalDateTime localDateTime = ZonedDateTime.of(2024, 3, 16, 20, 15, 30, 0, ZoneId.of("Asia/Kolkata"))
        .toLocalDateTime();

    return Stream.of(
        Arguments.of(localDateTime, DateTimeConstant.Format.SIMPLE_DATE, "2024-03-16"),
        Arguments.of(localDateTime, DateTimeConstant.Format.SIMPLE_TIME, "20:15:30"),
        Arguments.of(localDateTime, DateTimeConstant.Format.SIMPLE_DATE_TIME, "2024-03-16 20:15:30"),
        Arguments.of(localDateTime, DateTimeConstant.Format.ISO_DATE, "2024-03-16Z"),
        Arguments.of(localDateTime, DateTimeConstant.Format.ISO_TIME, "20:15:30Z"),
        Arguments.of(localDateTime, DateTimeConstant.Format.ISO_DATE_TIME, "2024-03-16T20:15:30Z")
    );
  }

  @ParameterizedTest
  @MethodSource("shouldReturnDateTimeParameters")
  public void shouldReturnDateTime_toDateTime(String dateTimeString, DateTimeConstant.Format format, LocalDateTime dateTime) {

    Assertions.assertEquals(dateTime, DateTimeUtility.toDateTime(dateTimeString, format));
  }

  public static Stream<Arguments> shouldReturnDateTimeParameters() {

    LocalDateTime localDateTime = ZonedDateTime.of(2024, 3, 16, 20, 15, 30, 0, ZoneId.of("Asia/Kolkata"))
        .toLocalDateTime();

    return Stream.of(
        Arguments.of("2024-03-16 20:15:30", DateTimeConstant.Format.SIMPLE_DATE_TIME, localDateTime),
        Arguments.of("2024-03-16T20:15:30", DateTimeConstant.Format.ISO_DATE_TIME, localDateTime),
        Arguments.of("2024-03-16T20:15:30Z", DateTimeConstant.Format.ISO_DATE_TIME, localDateTime),
        Arguments.of("2024-03-16T20:15:30+05:30", DateTimeConstant.Format.ISO_DATE_TIME, localDateTime)
    );
  }

  @ParameterizedTest
  @MethodSource("shouldReturnDateParameters")
  public void shouldReturnDate_toDate(String dateString, DateTimeConstant.Format format, LocalDate date) {

    Assertions.assertEquals(date, DateTimeUtility.toDate(dateString, format));
  }

  public static Stream<Arguments> shouldReturnDateParameters() {

    LocalDate localDate = ZonedDateTime.of(2024, 3, 16, 20, 15, 30, 0, ZoneId.of("Asia/Kolkata"))
        .toLocalDate();

    return Stream.of(
        Arguments.of("2024-03-16", DateTimeConstant.Format.SIMPLE_DATE, localDate),
        Arguments.of("2024-03-16 20:15:30", DateTimeConstant.Format.SIMPLE_DATE_TIME, localDate),
        Arguments.of("2024-03-16", DateTimeConstant.Format.ISO_DATE, localDate),
        Arguments.of("2024-03-16T20:15:30", DateTimeConstant.Format.ISO_DATE_TIME, localDate),
        Arguments.of("2024-03-16T20:15:30Z", DateTimeConstant.Format.ISO_DATE_TIME, localDate),
        Arguments.of("2024-03-16T20:15:30+05:30", DateTimeConstant.Format.ISO_DATE_TIME, localDate)
    );
  }
}
