package com.exatask.platform.utilities.unit;

import com.exatask.platform.utilities.NumberUtility;
import org.instancio.Gen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumberUtilityTest {

  @Test
  public void shouldReturnTrueFalse_isEmpty() {

    Assertions.assertAll(
        () -> Assertions.assertTrue(NumberUtility.isEmpty((Integer) null)),
        () -> Assertions.assertTrue(NumberUtility.isEmpty((Double) null)),
        () -> Assertions.assertFalse(NumberUtility.isEmpty(0)),
        () -> Assertions.assertFalse(NumberUtility.isEmpty(Gen.ints().max(-1).get())),
        () -> Assertions.assertFalse(NumberUtility.isEmpty(Gen.ints().min(1).get())),
        () -> Assertions.assertFalse(NumberUtility.isEmpty(0.0)),
        () -> Assertions.assertFalse(NumberUtility.isEmpty(Gen.doubles().max(-0.1).get())),
        () -> Assertions.assertFalse(NumberUtility.isEmpty(Gen.doubles().min(0.1).get()))
    );
  }

  @Test
  public void shouldReturnTrueFalse_isNotEmpty() {

    Assertions.assertAll(
        () -> Assertions.assertFalse(NumberUtility.isNotEmpty((Integer) null)),
        () -> Assertions.assertFalse(NumberUtility.isNotEmpty((Double) null)),
        () -> Assertions.assertTrue(NumberUtility.isNotEmpty(0)),
        () -> Assertions.assertTrue(NumberUtility.isNotEmpty(Gen.ints().max(-1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNotEmpty(Gen.ints().min(1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNotEmpty(0.0)),
        () -> Assertions.assertTrue(NumberUtility.isNotEmpty(Gen.doubles().max(-0.1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNotEmpty(Gen.doubles().min(0.1).get()))
    );
  }

  @Test
  public void shouldReturnTrueFalse_isNonZero() {

    Assertions.assertAll(
        () -> Assertions.assertFalse(NumberUtility.isNonZero((Integer) null)),
        () -> Assertions.assertFalse(NumberUtility.isNonZero((Double) null)),
        () -> Assertions.assertFalse(NumberUtility.isNonZero(0)),
        () -> Assertions.assertFalse(NumberUtility.isNonZero(0.0)),
        () -> Assertions.assertTrue(NumberUtility.isNonZero(Gen.ints().max(-1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNonZero(Gen.ints().min(1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNonZero(Gen.doubles().max(-0.1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNonZero(Gen.doubles().min(0.1).get()))
    );
  }

  @Test
  public void shouldReturnTrueFalse_isPositive() {

    Assertions.assertAll(
        () -> Assertions.assertFalse(NumberUtility.isPositive((Integer) null)),
        () -> Assertions.assertFalse(NumberUtility.isPositive((Double) null)),
        () -> Assertions.assertFalse(NumberUtility.isPositive(0)),
        () -> Assertions.assertFalse(NumberUtility.isPositive(0.0)),
        () -> Assertions.assertFalse(NumberUtility.isPositive(Gen.ints().max(-1).get())),
        () -> Assertions.assertFalse(NumberUtility.isPositive(Gen.doubles().max(-0.1).get())),
        () -> Assertions.assertTrue(NumberUtility.isPositive(Gen.ints().min(1).get())),
        () -> Assertions.assertTrue(NumberUtility.isPositive(Gen.doubles().min(0.1).get()))
    );
  }

  @Test
  public void shouldReturnTrueFalse_isNegative() {

    Assertions.assertAll(
        () -> Assertions.assertFalse(NumberUtility.isNegative((Integer) null)),
        () -> Assertions.assertFalse(NumberUtility.isNegative((Double) null)),
        () -> Assertions.assertFalse(NumberUtility.isNegative(0)),
        () -> Assertions.assertFalse(NumberUtility.isNegative(0.0)),
        () -> Assertions.assertFalse(NumberUtility.isNegative(Gen.ints().min(1).get())),
        () -> Assertions.assertFalse(NumberUtility.isNegative(Gen.doubles().min(0.1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNegative(Gen.ints().max(-1).get())),
        () -> Assertions.assertTrue(NumberUtility.isNegative(Gen.doubles().max(-0.1).get()))
    );
  }
}
