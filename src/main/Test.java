package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Random;

public class Test {
  public static void main(String[] args) {
    //mainTest("english.50MB.gz");
    testSuffixArray();
  }
  
  @SuppressWarnings("unused")
  private static void testSuffixArray() {
    String text1 = "mississippi";
    String text2 = "banana";
    String text3 = "cattcat";
    String text4 = "attcatg";
    String text5 = "hola mundo";
    String text = text5;
    SuffixArray suf = new SuffixArray(normalizeText(text));
    System.out.print("SuffixArray: ");
    for (int i = 0; i < suf.length(); i++) {
      System.out.print(suf.get(i) + " ");
    }
    String[] words = {"hola", "mundo"};
    System.out.println(String.join("/", words));
  }
  
  private static String normalizeText(String text) {
    text = text.toLowerCase();
    text = Normalizer.normalize(text, Normalizer.Form.NFD);
    text = text.replaceAll("[^0-9a-z ]", "");
    text = text + '\u0000';
    return text;
  }
  
  @SuppressWarnings("unused")
  private static void mainTest(String filename) {
    BufferedReader br = null;
    FileReader fr = null;
    BufferedWriter bw = null;
    FileWriter fw = null;
    try {
        fr = new FileReader(filename);
        br = new BufferedReader(fr);
        String baseText = "";
        String sCurrentLine;
        String[] words;
        Random rnd = new Random();
        
        while ((sCurrentLine = br.readLine()) != null) {
          baseText += sCurrentLine;  
        }
        baseText = normalizeText(baseText);
        words = baseText.split(" ");
        
        for (int i = 15; i <= 25; i++) {
          System.out.println("Proccessing i = " + i);
          int N = (int) Math.pow(2, i);
          String text = String.join(" ", Arrays.copyOfRange(words, 0, N ));
          long constructionStart = System.currentTimeMillis();
          SuffixArray suffixArray = new SuffixArray(text);
          long constructionTime = System.currentTimeMillis() - constructionStart;
          
          try {
            fw = new FileWriter("results_ " + i);
            bw = new BufferedWriter(fw);
            String word;
            int[] occurrencies;
            int length;
            long searchingStart, searchingTime = 0;
            
            for (int j = 0; j < N/10; j++) {
              word = words[rnd.nextInt(N)];
              length = suffixArray.length();
              searchingStart = System.currentTimeMillis();
              occurrencies = searchWord(suffixArray, text, word, 0, length);
              searchingTime += (System.currentTimeMillis() - searchingStart)/word.length();
            }
            bw.write("Construction time: " + constructionTime);
            bw.newLine();
            bw.write("Searching time: " + searchingTime + " * pattern length");
          } catch (IOException e) {
            e.printStackTrace();
          } finally {
            try {
              if (bw != null)
                  bw.close();
              if (fw != null)
                  fw.close();
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            if (br != null)
                br.close();
            if (fr != null)
                fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
  }
  
  private static int[] searchWord(SuffixArray suffixarray, String text, String word, int i, int j) {
    int m = (i+j)/2;
    int index = suffixarray.get(m);
    String candidate = text.substring(index, index + word.length());
    if (word.equals(candidate)) {
      for (int k = m+1; k+word.length() < suffixarray.length(); k++) {
        
      }
    }
    return null;
  }
}
