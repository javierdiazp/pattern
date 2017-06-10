package main;

import java.text.Normalizer;

public class Test {
  public static void main(String[] args) {
    testSuffixArray();
  }
  
  @SuppressWarnings("unused")
  private static void testSuffixArray() {
    String text = "mississippi";
    SuffixArray suf = new SuffixArray(normalizeText(text));
  }
  
  private static String normalizeText(String text) {
    text = text.toLowerCase();
    text = Normalizer.normalize(text, Normalizer.Form.NFD);
    text = text.replaceAll("[^0-9a-z ]", "");
    text = text + "$";
    return text;
  }
}
