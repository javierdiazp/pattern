package main;

import java.text.Normalizer;

public class Test {
  public static void main(String[] args) {
    testSuffixArray();
  }
  
  @SuppressWarnings("unused")
  private static void testSuffixArray() {
    String text1 = "mississippi";
    String text2 = "banana";
    String text3 = "trabajo";
    String text4 = "cattcat";
    String text5 = "attcatg";
    String text = text1;
    SuffixArray suf = new SuffixArray(normalizeText(text));
    System.out.print("SuffixArray: ");
    for (int i = 0; i < suf.length(); i++) {
      System.out.print(suf.get(i) + " ");
    }
    
  }
  
  private static String normalizeText(String text) {
    text = text.toLowerCase();
    text = Normalizer.normalize(text, Normalizer.Form.NFD);
    text = text.replaceAll("[^0-9a-z ]", "");
    text = text + '\u0000';
    return text;
  }
}
