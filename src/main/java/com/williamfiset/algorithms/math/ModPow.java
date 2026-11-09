/**
 * Computes modular exponentiation: a^n mod m.
 *
 * Supports negative exponents via modular inverse (requires gcd(a, m) = 1) and negative bases.
 * Uses overflow-safe modular multiplication to handle the full range of long values.
 *
 * Time Complexity: O(log(n))
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 */
package com.williamfiset.algorithms.math;

public class ModPow {

  /**
   * Computes a^n mod m.
   *
   * @throws ArithmeticException if mod <= 0, or if n < 0 and gcd(a, mod) != 1.
   */
  public static long modPow(long a, long n, long mod) {
    if (mod <= 0)
      throw new ArithmeticException("mod must be > 0");

    // a^-n mod m = (a^-1)^n mod m
    if (n < 0) {
      if (gcd(a, mod) != 1)
        throw new ArithmeticException("If n < 0 then must have gcd(a, mod) = 1");
      a = modInv(a, mod);
      n = -n;
    }

    // Overflow-safe normalization of base into [0, mod)
    a %= mod;
    if (a < 0)
      a += mod;

    long result = 1 % mod;
    while (n != 0) {
      if ((n & 1) == 1)
        result = mulMod(result, a, mod);
      a = mulMod(a, a, mod);
      n >>>= 1;
    }
    return result;
  }

  private static long modInv(long a, long m) {
    a %= m;
    if (a < 0)
      a += m;
    long x = egcd(a, m)[1] % m;
    return x < 0 ? x + m : x;
  }

  private static long[] egcd(long a, long b) {
    if (b == 0)
      return new long[] { a < 0 ? -a : a, 1L, 0L };
    long[] v = egcd(b, a % b);
    long tmp = v[1] - v[2] * (a / b);
    v[1] = v[2];
    v[2] = tmp;
    return v;
  }

  private static long gcd(long a, long b) {
    return b == 0 ? Math.abs(a) : gcd(b, a % b);
  }

  /** Overflow-safe modular multiplication: (a * b) % mod. */
  private static long mulMod(long a, long b, long mod) {
    return java.math.BigInteger.valueOf(a)
        .multiply(java.math.BigInteger.valueOf(b))
        .mod(java.math.BigInteger.valueOf(mod))
        .longValue();
  }
}