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
    mainTest("english.50MB");
    //testSuffixArray();
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
  }
  
  private static String normalizeText(String text) {
    text = text.toLowerCase();
    text = Normalizer.normalize(text, Normalizer.Form.NFD);
    text = text.replaceAll("[^0-9a-z ]", "");
    text = text + '\u0000';
    return text;
  }
  
  //@SuppressWarnings("unused")
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
        
        System.out.println("Reading file...");
        while ((sCurrentLine = br.readLine()) != null) {
          baseText += sCurrentLine;
          System.out.println(sCurrentLine);
        }
        System.out.println("Proccessing text...");
        baseText = normalizeText(baseText);
        System.out.println("Calculating words...");
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
            int occurrences;
            int length;
            long searchingStart, searchingTime = 0;
            bw.write("Test N = 2^" + i);
            bw.newLine(); bw.newLine();
            bw.write("Ocurrences:");
            bw.newLine();
            for (int j = 0; j < N/10; j++) {
              
              word = words[rnd.nextInt(N)];
              length = suffixArray.length();
              searchingStart = System.currentTimeMillis();
              occurrences = searchWord(suffixArray, text, word, 0, length);
              bw.write(word + ": " + occurrences);
              bw.newLine();
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
  
  private static int searchWord(SuffixArray suffixarray, String text, String word, int i, int j) {
    int occurrences = 0;
    int m = (i+j)/2;
    int index = suffixarray.get(m);
    String candidate = text.substring(index, index + word.length());
    int cmp = word.compareTo(candidate);
    
    if ( !(i < j)) return occurrences;
    
    if (cmp == 0) {
      occurrences++;
      for (int k = m+1; k < j; k++) {
        candidate = text.substring(k, k + word.length());
        if (word.equals(candidate)) {
          occurrences++;
        } else {
          break;
        }
      }
      
      for (int k = m-1; k >= i; k--) {
        candidate = text.substring(k, k + word.length());
        if (word.equals(candidate)) {
          occurrences++;
        } else {
          break;
        }
      }
    } else {
      if (cmp < 0) {
        occurrences += searchWord(suffixarray, text, word, m+1, j);
      } else {
        occurrences += searchWord(suffixarray, text, word, i, m);
      }
    }
    return occurrences;
  }
}
