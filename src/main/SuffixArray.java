package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SuffixArray {
  private final char SPEC = '$';
  private final int ALPHABETSIZE = Character.getNumericValue('z') + 1;
  private String text;
  
  public SuffixArray(String text) {
    this.text = text;
  }
  
  private String tokenizeGroup(String text, int group) {
    List<Integer>[] bucketArray = new ArrayList[3];
    
    
    int[] triple = new int[text.length()/3];

    String currTriple;
    for (int i = 2; i >= 0; i--) {
      for (int j = group; j < triple.length; j++) {
        currTriple = text.substring(j, j+3);
      }
    }
    
    
    Comparator<Integer> cmp = new Comparator<Integer>() {
      @Override
      public int compare(Integer i0, Integer i1) {
        String s0 = text.substring(i0, i0+3);
        String s1 = text.substring(i1, i1+3);
        return s0.compareTo(s1);
      }
    };
    // FIXME hay que usar radix sort
    return "";
  }
  
  public int[] radixSort(int[] indexArray) {
    List<List<Integer>> bucketList = new ArrayList<List<Integer>>();
    String triple;
    int index;
    
    for (int i = 0; i < ALPHABETSIZE; i++) {
      bucketList.add(new ArrayList<Integer>());
    }
    
    for (int i = 2; i >= 0; i--) {
      for (int j = 0; j < indexArray.length; j++) {
        index = indexArray[j];
        triple = this.text.substring(index, index + 3);
        bucketList.get(Character.getNumericValue(triple.charAt(i))).add(index);
      }
      
      int c = 0;
      System.out.println("ITER"+i+": ");
      for (int j = 0; j < indexArray.length; j++) {
        System.out.println(indexArray[j]);
      }
      for (int j = 0; j < bucketList.size(); j++) {
        for (int k = 0; k < bucketList.get(j).size(); k++) {
          System.out.println("  " + bucketList.get(j).get(k));
          indexArray[c] = bucketList.get(j).get(k);
          bucketList.get(j).clear();
        }
      }
    }
    return indexArray;
  }
  
  public String getText() {
    return this.text;
  }
}
