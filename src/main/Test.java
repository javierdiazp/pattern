package main;

import java.text.Normalizer;

public class Test {
  public static void main(String[] args) {
    String text = "aaaaabaac";
    int[] indexArray = {0, 3, 6};
    
    text = normalizeText(text);
    SuffixArray suf = new SuffixArray(text);
    indexArray = suf.radixSort(indexArray);
  }
  
  private static String normalizeText(String text) {
    text = text.toLowerCase();
    text = Normalizer.normalize(text, Normalizer.Form.NFD);
    text = text.replaceAll("[^0-9a-z ]", "");
    return text;
  }
}
