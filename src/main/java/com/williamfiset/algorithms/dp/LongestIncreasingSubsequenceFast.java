package com.williamfiset.algorithms.dp;

/**
 * Finds the longest increasing subsequence within an array of numbers obtained by removing the
 * fewest possible elements from the initial array. Complexity: O(n * log n)
 */
public class LongestIncreasingSubsequenceFast {
  /** Main function. */
  public static void main(String[] args) {

    System.out.println(longestIncreasingSubsequenceLength(new int[] {1, 3, 2, 4, 3})); // 3
    System.out.println(longestIncreasingSubsequenceLength(new int[] {2, 7, 4, 3, 8})); // 3
    System.out.println(longestIncreasingSubsequenceLength(new int[] {5, 4, 3, 2, 1})); // 1
    System.out.println(
        longestIncreasingSubsequenceLength(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9})); // 9
    System.out.println(
        longestIncreasingSubsequenceLength(
            new int[] {0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 3, 11, 7, 15})); // 6
  }

  /**
   * Function performing the binary search step of the main algorithm.
   *
   * @param ar parsed array
   * @param l lower bound representing the leftmost index of the array
   * @param r upper bound representing the rightmost index of the array
   * @param key value to be inserted
   * @return index before which the key can be inserted
   */
  static int binarySearch(int[] ar, int l, int r, int key) {
    while (r - l > 1) {
      int m = l + (r - l) / 2;
      if (ar[m] >= key) {
        r = m;
      } else {
        l = m;
      }
    }
    return r;
  }

  /**
   * Function computing the length of the longest increasing subsequence from an array.
   *
   * @param ar initial array
   * @return the length of the longest increasing subsequence
   */
  static int longestIncreasingSubsequenceLength(int[] ar) {
    int size = ar.length;
    if (size == 0) {
      return 0;
    }
    int[] lis = new int[size];
    int len;
    lis[0] = ar[0];
    len = 1;
    for (int i = 1; i < size; i++) {
      // minimum value needs updating
      if (ar[i] < lis[0]) {
        lis[0] = ar[i];
      } else if (ar[i] > lis[len - 1]) { // new value can be safely appended
        lis[len++] = ar[i];
      } else { // position needs to be found for new value
        lis[binarySearch(lis, -1, len - 1, ar[i])] = ar[i];
      }
    }
    return len;
  }
}
