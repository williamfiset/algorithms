package com.williamfiset.algorithms.strings;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class AhoCorasickTest {

  @Test
  public void basicMatching() {
    AhoCorasick ac = new AhoCorasick();
    ac.addPattern("he");
    ac.addPattern("she");
    ac.addPattern("his");
    ac.addPattern("hers");
    ac.build();

    List<AhoCorasick.Match> matches = ac.search("ushers");

    // Expected: "she", "he", "hers"
    assertEquals(3, matches.size());

    assertEquals(1, matches.get(0).index);
    assertEquals(3, matches.get(0).length);

    assertEquals(2, matches.get(1).index);
    assertEquals(2, matches.get(1).length);

    assertEquals(2, matches.get(2).index);
    assertEquals(4, matches.get(2).length);
  }

  @Test
  public void overlappingPatterns() {
    AhoCorasick ac = new AhoCorasick();
    ac.addPattern("aba");
    ac.addPattern("ba");
    ac.build();

    List<AhoCorasick.Match> matches = ac.search("ababa");

    assertEquals(4, matches.size());

    assertEquals(0, matches.get(0).index); // "aba"
    assertEquals(1, matches.get(1).index); // "ba"
    assertEquals(2, matches.get(2).index); // "aba"
    assertEquals(3, matches.get(3).index); // "ba"
  }

  @Test
  public void noMatches() {
    AhoCorasick ac = new AhoCorasick();
    ac.addPattern("cat");
    ac.addPattern("dog");
    ac.build();

    List<AhoCorasick.Match> matches = ac.search("aaaaa");
    assertTrue(matches.isEmpty());
  }

  @Test
  public void searchWithoutBuildThrows() {
    AhoCorasick ac = new AhoCorasick();
    ac.addPattern("abc");

    assertThrows(IllegalStateException.class, () -> ac.search("abc"));
  }
}
