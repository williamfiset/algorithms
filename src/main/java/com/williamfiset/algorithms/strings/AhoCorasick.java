package com.williamfiset.algorithms.strings;

import java.util.*;

/**
 * Aho–Corasick algorithm
 *
 * <p>Multi-pattern string matching in O(n + m + z): n = text length, m = total pattern length, z =
 * number of matches.
 *
 * <p>Typical use cases: spam filters, IDS, virus scanning, keyword detection.
 */
public class AhoCorasick {

  private final Node root = new Node();
  private boolean built = false;

  // Example usage
  public static void main(String[] args) {
    AhoCorasick ac = new AhoCorasick();
    ac.addPattern("he");
    ac.addPattern("she");
    ac.addPattern("his");
    ac.addPattern("hers");

    ac.build();

    String text = "ushers";
    List<Match> matches = ac.search(text);

    for (Match m : matches) {
      System.out.println(
          "Match \"" + text.substring(m.index, m.index + m.length) + "\" at index " + m.index);
    }
  }

  /** Insert a pattern (call build() once all patterns are added). */
  public void addPattern(String word) {
    built = false;
    Node node = root;
    for (char c : word.toCharArray()) {
      Node next = node.children.get(c);
      if (next == null) {
        next = new Node();
        node.children.put(c, next);
      }
      node = next;
    }
    node.output.add(word);
  }

  /** Build failure links (BFS). Must be called before search(). */
  public void build() {
    Queue<Node> q = new ArrayDeque<Node>();

    // Level 1 → fail points to root
    for (Node child : root.children.values()) {
      child.fail = root;
      q.add(child);
    }

    while (!q.isEmpty()) {
      Node node = q.remove();

      for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
        char c = entry.getKey();
        Node nxt = entry.getValue();
        q.add(nxt);

        Node f = node.fail;
        while (f != null && !f.children.containsKey(c)) {
          f = f.fail;
        }

        nxt.fail = (f == null) ? root : f.children.get(c);
        nxt.output.addAll(nxt.fail.output); // inherit matches
      }
    }

    built = true;
  }

  /** Search text and return a list of matches (index, length). */
  public List<Match> search(String text) {
    if (!built) throw new IllegalStateException("Call build() before search().");

    List<Match> results = new ArrayList<Match>();
    Node node = root;

    for (int i = 0; i < text.length(); i++) {
      char c = text.charAt(i);

      while (node != null && !node.children.containsKey(c)) {
        node = node.fail;
      }

      if (node == null) node = root;
      else node = node.children.get(c);

      for (String w : node.output) {
        results.add(new Match(i - w.length() + 1, w.length()));
      }
    }

    return results;
  }

  // Match result
  public static class Match {
    public final int index;
    public final int length;

    Match(int index, int length) {
      this.index = index;
      this.length = length;
    }
  }

  // Trie node
  private static class Node {
    Map<Character, Node> children = new HashMap<>();
    Node fail;
    List<String> output = new ArrayList<>();
  }
}
