package main;

import java.util.ArrayList;
import java.util.List;

public class SuffixArray {
  private final char SPEC = '$';
  private final int ALPHABETSIZE = Character.getNumericValue('z') + 2;
  private String text;
  
  public SuffixArray(String text) {
    while (text.length()%3 != 1) {
      text = text + SPEC;
    }
    this.text = text;
    
    int[] arr = {1, 4, 7, 10, 2, 5, 8};
    System.out.println(tokenize(arr));
  }
  
  private String tokenize(int[] textIndexArray) {
    char[] tokenArray = new char[textIndexArray.length];
    int[] indexIndexArray = radixSort(textIndexArray);
    int token = 0;
    int index;
    String lastTriple = null;
    String currTriple;
    
    for (int i = 0; i < indexIndexArray.length; i++) {
      index = textIndexArray[indexIndexArray[i]];
      currTriple = this.text.substring(index, index + 3);
      if (currTriple.equals(lastTriple)) {
        tokenArray[indexIndexArray[i]] = Character.forDigit(token, 36);
      } else {
        tokenArray[indexIndexArray[i]] = Character.forDigit(++token, 36);
      }
      lastTriple = currTriple;
    }
    return new String(tokenArray);
  }
  
  private int[] radixSort(int[] textIndexArray) {
    List<List<Pair>> bucketList = new ArrayList<List<Pair>>();
    Pair[] indexPairArray = new Pair[textIndexArray.length];
    Pair indexPair;
    String triple;
    int index;
    char symbol;
    
    /* create an array of pairs (textIndex, indexIndex) */
    for (int i = 0; i < indexPairArray.length; i++) {
      indexPairArray[i] = new Pair(textIndexArray[i], i);
    }
    
    /* initialize the list of buckets */
    for (int i = 0; i < ALPHABETSIZE; i++) {
      bucketList.add(new ArrayList<Pair>());
    }
    
    /* put pairs of indexes in the buckets */
    for (int i = 2; i >= 0; i--) {
      for (int j = 0; j < indexPairArray.length; j++) {
        indexPair = indexPairArray[j];
        index = indexPair.first;
        triple = this.text.substring(index, index + 3);
        symbol = triple.charAt(i);
        if (symbol == SPEC) {
          bucketList.get(0).add(indexPair);
        } else {
          bucketList.get(Character.getNumericValue(symbol)).add(indexPair);
        }
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
}
