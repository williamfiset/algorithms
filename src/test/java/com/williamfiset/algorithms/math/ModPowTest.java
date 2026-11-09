package com.williamfiset.algorithms.math;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.jupiter.api.*;

public class ModPowTest {

  @Test
  public void basicPositiveExponent() {
    // 3^4 mod 1000000 = 81
    assertThat(ModPow.modPow(3, 4, 1000000)).isEqualTo(81);
  }

  @Test
  public void negativeBase() {
    // (-45)^12345 mod 987654321
    long expected = BigInteger.valueOf(-45)
        .modPow(BigInteger.valueOf(12345), BigInteger.valueOf(987654321))
        .longValue();
    assertThat(ModPow.modPow(-45, 12345, 987654321)).isEqualTo(expected);
  }

  @Test
  public void negativeExponent() {
    // 6^-66 mod 101 = 84
    long expected = BigInteger.valueOf(6)
        .modPow(BigInteger.valueOf(-66), BigInteger.valueOf(101))
        .longValue();
    assertThat(ModPow.modPow(6, -66, 101)).isEqualTo(expected);
  }

  @Test
  public void negativeBaseAndExponent() {
    // (-5)^-7 mod 1009
    long expected = BigInteger.valueOf(-5)
        .modPow(BigInteger.valueOf(-7), BigInteger.valueOf(1009))
        .longValue();
    assertThat(ModPow.modPow(-5, -7, 1009)).isEqualTo(expected);
  }

  @Test
  public void exponentZero() {
    assertThat(ModPow.modPow(123, 0, 7)).isEqualTo(1);
    assertThat(ModPow.modPow(0, 0, 5)).isEqualTo(1);
  }

  @Test
  public void baseZero() {
    assertThat(ModPow.modPow(0, 10, 7)).isEqualTo(0);
  }

  @Test
  public void modOne() {
    // Anything mod 1 = 0
    assertThat(ModPow.modPow(999, 999, 1)).isEqualTo(0);
  }

  @Test
  public void largeValues() {
    // Test with values that would overflow without safe multiplication
    long a = 1_000_000_000L;
    long n = 1_000_000_000L;
    long mod = 999_999_937L;
    long expected = BigInteger.valueOf(a).modPow(BigInteger.valueOf(n), BigInteger.valueOf(mod)).longValue();
    assertThat(ModPow.modPow(a, n, mod)).isEqualTo(expected);
  }

  @Test
  public void modNonPositiveThrows() {
    assertThrows(ArithmeticException.class, () -> ModPow.modPow(2, 3, 0));
    assertThrows(ArithmeticException.class, () -> ModPow.modPow(2, 3, -5));
  }

  @Test
  public void negativeExponentNotCoprime() {
    // gcd(4, 8) = 4 ≠ 1, so no modular inverse
    assertThrows(ArithmeticException.class, () -> ModPow.modPow(4, -1, 8));
  }

  @Test
  public void matchesBigIntegerRandomized() {
    ThreadLocalRandom rng = ThreadLocalRandom.current();
    for (int i = 0; i < 500; i++) {
      long a = rng.nextLong(-1_000_000_000L, 1_000_000_000L);
      long n = rng.nextLong(0, 1_000_000_000L);
      long mod = rng.nextLong(1, 1_000_000_000L);
      long expected = BigInteger.valueOf(a).modPow(BigInteger.valueOf(n), BigInteger.valueOf(mod)).longValue();
      assertThat(ModPow.modPow(a, n, mod)).isEqualTo(expected);
    }
  }

  @Test
  public void modOneWithZeroExponent() {
    // 1 % mod must be used as the starting result, not 1
    assertThat(ModPow.modPow(5, 0, 1)).isEqualTo(0);
  }

  @Test
  public void exponentLongMinValue() {
    // -Long.MIN_VALUE overflows; negative exponent needs coprime base
    long mod = 1_000_000_007L;
    long expected = BigInteger.valueOf(3)
        .modPow(BigInteger.valueOf(Long.MIN_VALUE), BigInteger.valueOf(mod))
        .longValue();
    assertThat(ModPow.modPow(3, Long.MIN_VALUE, mod)).isEqualTo(expected);
  }

  @Test
  public void exponentLongMaxValue() {
    long mod = 1_000_000_007L;
    long expected = BigInteger.valueOf(3)
        .modPow(BigInteger.valueOf(Long.MAX_VALUE), BigInteger.valueOf(mod))
        .longValue();
    assertThat(ModPow.modPow(3, Long.MAX_VALUE, mod)).isEqualTo(expected);
  }

  @Test
  public void hugeModulus() {
    // mod > 2^62: (a % mod) + mod used to overflow
    long mod = Long.MAX_VALUE;
    long[] bases = { -5, -(Long.MAX_VALUE - 5), Long.MIN_VALUE, 3, Long.MAX_VALUE - 1 };
    for (long a : bases) {
      long expected = BigInteger.valueOf(a)
          .modPow(BigInteger.valueOf(12345), BigInteger.valueOf(mod))
          .longValue();
      assertThat(ModPow.modPow(a, 12345, mod)).isEqualTo(expected);
    }
  }

  @Test
  public void hugeModulusNegativeExponent() {
    long mod = Long.MAX_VALUE;
    long expected = BigInteger.valueOf(-3)
        .modPow(BigInteger.valueOf(-777), BigInteger.valueOf(mod))
        .longValue();
    assertThat(ModPow.modPow(-3, -777, mod)).isEqualTo(expected);
  }

  @Test
  public void matchesBigIntegerRandomizedFullRange() {
    ThreadLocalRandom rng = ThreadLocalRandom.current();
    for (int i = 0; i < 500; i++) {
      long a = rng.nextLong();
      long n = rng.nextLong(0, Long.MAX_VALUE);
      long mod = rng.nextLong(1, Long.MAX_VALUE);
      long expected = BigInteger.valueOf(a).modPow(BigInteger.valueOf(n), BigInteger.valueOf(mod)).longValue();
      assertThat(ModPow.modPow(a, n, mod)).isEqualTo(expected);
    }
  }

  @Test
  public void matchesBigIntegerRandomizedNegativeExponent() {
    ThreadLocalRandom rng = ThreadLocalRandom.current();
    int tested = 0;
    while (tested < 300) {
      long a = rng.nextLong();
      long n = rng.nextLong(Long.MIN_VALUE, 0);
      long mod = rng.nextLong(1, Long.MAX_VALUE);
      BigInteger bigA = BigInteger.valueOf(a);
      BigInteger bigMod = BigInteger.valueOf(mod);
      if (!bigA.gcd(bigMod).equals(BigInteger.ONE))
        continue; // no inverse, skip
      long expected = bigA.modPow(BigInteger.valueOf(n), bigMod).longValue();
      assertThat(ModPow.modPow(a, n, mod)).isEqualTo(expected);
      tested++;
    }
  }
}
