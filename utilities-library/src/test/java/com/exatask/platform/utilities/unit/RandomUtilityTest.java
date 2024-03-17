package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.RandomUtility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RandomUtilityTest {

  @Test
  public void shouldGenerateRandomString_alphaNumeric() {

    String randomString1 = RandomUtility.alphaNumeric(8);
    Assertions.assertEquals(8, randomString1.length());

    String randomString2 = RandomUtility.alphaNumeric(4);
    Assertions.assertEquals(4, randomString2.length());

    String randomString3 = RandomUtility.alphaNumeric(12);
    Assertions.assertEquals(12, randomString3.length());
  }
}
