package main;

import java.util.ArrayList;
import java.util.List;

public class SuffixArray {
  private final char SPEC = '\u0000'; // special character
  private final int ALPHABETSIZE = 128; // ASCII characters
  private final int[] ELEMENTS; // actual suffix array
  private final int LENGTH; // length of the suffix array
  
  public SuffixArray(String text) {
    if (text.length() == 0) {
      this.ELEMENTS = null;
      this.LENGTH = 0;
    } else {
      if (text.length() == 1) {
        this.ELEMENTS = new int[1];
        this.ELEMENTS[0] = 0;
        this.LENGTH = 1;
      } else {
        /* fill with special characters */
        text = text + SPEC + SPEC;
        
        int[] textIndexArray;
        
        /* tokenize group 1 and 2 */
        String tokens;
        textIndexArray = new int[(2*text.length())/3 - 1];
        int[] groups = {1, 2};
        int c = 0;
        for (int i: groups) {
          for (int j = i; j+2 < text.length(); j+=3) {
            textIndexArray[c] = j;
            c++;
          }
        }
        tokens = tokenize(text, textIndexArray);
        SuffixArray suffixArrayAB = new SuffixArray(tokens); // recursive step
        int[] group12 = new int[suffixArrayAB.length()];
        int half = (int) Math.ceil(1.0*suffixArrayAB.length()/2);
        
        c = 0;
        for (int i = 0; i < suffixArrayAB.length(); i++) {
          if (suffixArrayAB.get(i) < half) {
            group12[c++] = suffixArrayAB.get(i)*3 + 1;
          } else {
            group12[c++] = (suffixArrayAB.get(i)-half)*3 + 2;
          }
        }
        
        /* handle group 0 */
        textIndexArray = new int[text.length()/3];
        c = 0;
        for (int i = 0; i+2 < text.length(); i+=3) {
          textIndexArray[c] = i;
          c++;
        }
        int[] indexIndexArray = this.radixSort(text, textIndexArray, false, tokens);
        int[] group0 = new int[indexIndexArray.length];
        for (int i = 0; i < group0.length; i++) {
          group0[i] = textIndexArray[indexIndexArray[i]];
        }
        
        /* merge group 1 and 2 with group 0 */
        int[] elements = new int[group12.length + group0.length];
        int counter = 0, counter12 = 0, counter0 = 0;
        int elem12, elem0;
        
        while (counter12 < group12.length && counter0 < group0.length) {
          elem12 = group12[counter12];
          elem0 = group0[counter0];
          
          if (text.charAt(elem12) != text.charAt(elem0)) {
            if (text.charAt(elem12) < text.charAt(elem0)) {
              elements[counter] = elem12;
              counter12++;
            } else {
              elements[counter] = elem0;
              counter0++;
            }
          } else {
            if (elem12%3 == 1) { // group 1
              for (int i = 0; i < group12.length; i++) {
                if (group12[i] == elem12 + 1) {
                  elements[counter] = elem12;
                  counter12++;
                  break;
                }
                if (group12[i] == elem0 + 1) {
                  elements[counter] = elem0;
                  counter0++;
                  break;
                }
              }
            } else { // group 2 (compare other character)
              if (text.charAt(elem12+1) != text.charAt(elem0+1)) {
                if (text.charAt(elem12+1) < text.charAt(elem0+1)) {
                  elements[counter] = elem12;
                  counter12++;
                } else {
                  elements[counter] = elem0;
                  counter0++;
                }
              } else {
                for (int i = 0; i < group12.length; i++) {
                  if (group12[i] == elem12 + 2) {
                    elements[counter] = elem12;
                    counter12++;
                    break;
                  }
                  if (group12[i] == elem0 + 2) {
                    elements[counter] = elem0;
                    counter0++;
                    break;
                  }
                }
              }
            }
          }
          counter++;
        }
        
        while (counter12 < group12.length) {
          elem12 = group12[counter12];
          elements[counter] = elem12;
          counter12++;
          counter++;
        }
        
        while (counter0 < group0.length) {
          elem0 = group0[counter0];
          elements[counter] = elem0;
          counter0++;
          counter++;
        }
        
        this.ELEMENTS = elements;
        this.LENGTH = elements.length;
      }
    }
  }
  
  private String tokenize(String text, int[] textIndexArray) {
    char[] tokenArray = new char[textIndexArray.length];
    int[] indexIndexArray = this.radixSort(text, textIndexArray, true, null);
    int token = 0;
    int index;
    String lastTriple = null;
    String currTriple;
    
    for (int i = 0; i < indexIndexArray.length; i++) {
      index = textIndexArray[indexIndexArray[i]];
      currTriple = text.substring(index, index + 3);
      if (currTriple.equals(lastTriple)) {
        tokenArray[indexIndexArray[i]] = (char) token;
      } else {
        tokenArray[indexIndexArray[i]] = (char) ++token;
      }
      lastTriple = currTriple;
    }
    return new String(tokenArray);
  }

  private int[] radixSort(String text, int[] textIndexArray, boolean mode, String tokens) {
    List<List<Pair>> bucketList = new ArrayList<List<Pair>>();
    Pair[] indexPairArray = new Pair[textIndexArray.length];
    Pair indexPair;
    String triple;
    int index;
    
    /* create an array of pairs (textIndex, indexIndex) */
    for (int i = 0; i < indexPairArray.length; i++) {
      indexPairArray[i] = new Pair(textIndexArray[i], i);
    }
    
    /* initialize the list of buckets */
    for (int i = 0; i < ALPHABETSIZE; i++) {
      bucketList.add(new ArrayList<Pair>());
    }
    
    /* put pairs of indexes in the buckets */
    int init = 1;
    if (mode) init = 2;
    for (int i = init; i >= 0; i--) {
      for (int j = 0; j < indexPairArray.length; j++) {
        indexPair = indexPairArray[j];
        index = indexPair.first;
        if (mode) {
          triple = text.substring(index, index + 3);
        } else {
          triple = text.substring(index, index + 1) + tokens.charAt(index/3);
        }
        bucketList.get((int) triple.charAt(i)).add(indexPair);
      }
      
      int c = 0;
      for (int j = 0; j < bucketList.size(); j++) {
        for (int k = 0; k < bucketList.get(j).size(); k++) {
          indexPairArray[c] = bucketList.get(j).get(k);
          c++;
        }
        bucketList.get(j).clear();
      }
    }
    
    /* extract the last index from the array of pairs */
    int[] indexIndexArray = new int[indexPairArray.length];
    for (int i = 0; i < indexPairArray.length; i++) {
      indexIndexArray[i] = indexPairArray[i].last;
    }
    
    return indexIndexArray;
  }
  
  private class Pair {
    private final Integer first;
    private final Integer last;
    
    private Pair(Integer first, Integer last) {
      this.first = first;
      this.last = last;
    }
  }
  
  public int get(int i) {
    return this.ELEMENTS[i];
  }
  
  public int length() {
    return this.LENGTH;
  }
}
