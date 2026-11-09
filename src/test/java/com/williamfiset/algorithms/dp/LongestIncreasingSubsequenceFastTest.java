package com.williamfiset.algorithms.dp;

import org.junit.Assert;
import org.junit.Test;

/**
 * Checking that the returned values of the two different implementations are equal (consistent).
 */
public class LongestIncreasingSubsequenceFastTest {
  @Test
  public void longestIncreasingSubsequenceFastEmpty() {
    int[] array = {};
    Assert.assertEquals(
        0, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
    Assert.assertEquals(
        LongestIncreasingSubsequence.lis(array),
        LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
  }

  @Test
  public void longestIncreasingSubsequenceFastSingle() {
    int[] array = {3};
    Assert.assertEquals(
        1, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
    Assert.assertEquals(
        LongestIncreasingSubsequence.lis(array),
        LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
  }

  @Test
  public void longestIncreasingSubsequenceFastRandom() {
    int[][] array = {
      {1, 3, 2, 4, 3},
      {2, 7, 4, 3, 8},
      {0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15}
    };

    Assert.assertEquals(
        3, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array[0]));
    Assert.assertEquals(
        3, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array[1]));
    Assert.assertEquals(
        6, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array[2]));

    for (int[] ints : array) {
      Assert.assertEquals(
          LongestIncreasingSubsequence.lis(ints),
          LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(ints));
    }
  }

  @Test
  public void longestIncreasingSubsequenceFastAscending() {
    int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    Assert.assertEquals(
        9, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
    Assert.assertEquals(
        LongestIncreasingSubsequence.lis(array),
        LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
  }

  @Test
  public void longestIncreasingSubsequenceFastDescending() {
    int[] array = {5, 4, 3, 2, 1};
    Assert.assertEquals(
        1, LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
    Assert.assertEquals(
        LongestIncreasingSubsequence.lis(array),
        LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(array));
  }

  @Test(expected = NullPointerException.class)
  public void longestIncreasingSubsequenceFastNull() {
    LongestIncreasingSubsequenceFast.longestIncreasingSubsequenceLength(null);
    LongestIncreasingSubsequence.lis(null);
  }
}
