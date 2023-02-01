package com.exatask.platform.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NumberUtility {

  public static Boolean isEmpty(Integer number) {
    return number == null;
  }

  public static Boolean isNotEmpty(Integer number) {
    return number != null;
  }

  public static Boolean isNonZero(Integer number) {
    return isNotEmpty(number) && number != 0;
  }

  public static Boolean isPositive(Integer number) {
    return isNotEmpty(number) && number > 0;
  }

  public static Boolean isEmpty(Double number) {
    return number == null;
  }

  public static Boolean isNotEmpty(Double number) {
    return number != null;
  }

  public static Boolean isNonZero(Double number) {
    return isNotEmpty(number) && number != 0.0;
  }

  public static Boolean isPositive(Double number) {
    return isNotEmpty(number) && number > 0.0;
  }
}
