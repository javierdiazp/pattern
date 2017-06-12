package main;

public class Automata {
  private String alphabet = "abcdefghijklmnopqrstuvwxyz";
  private int previousX = 0;
  private int previousState = 0;
  
  private char[] initialPattern;
  private int[][] transitions;
  
  public Automata(String pattern) {
    initialPattern = new char[pattern.length() + 1];
    char explorer = pattern.charAt(0);
    for (int i = 1; i < initialPattern.length; i++) {
      initialPattern[i] = explorer;
      if (i < pattern.length()) {
        explorer = pattern.charAt(i);
      }
    }
    createTransitionFunction(pattern);
  }
  
  private void createTransitionFunction(String pattern) {
    transitions = new int[alphabet.length()][pattern.length() + 1];
    for (int i = 0; i < initialPattern.length; i++) {
      for (int j = 0; j < alphabet.length(); j++) {
        transition(pattern, i, alphabet.charAt(j), transitions);
      }
    }
  }
  
  public void transition(String pattern, int state, char a, int[][] transitions) {
    if (pattern.indexOf(a) == -1) {
      return;
    }
    
    if (state != pattern.length() && pattern.charAt(state) == a) {
       transitions[alphabet.indexOf(a)][state] = state + 1; //if P[q+1]=a, delta(q,a)=q+1
    }
    
    else if (state >= 1) {
      int x = 0;
      String search = pattern.substring(1, state); //P[2:q]
      if (state != previousState) { //actualizar X al cambiar de estado
        for (int i = 0; i < search.length(); i++) {
          x = run(x, search.charAt(i)); //correr automata sobre P[2:q]
          previousX = x;
        }
      }
      x = run(previousX, a); //calcular delta(X,a)
      transitions[alphabet.indexOf(a)][state] = x;
    }
  }
  
  public int run(int q, char a) {
    return transitions[alphabet.indexOf(a)][q];
  }
  
  public char[] getPattern() {
    return initialPattern;
  }
  
  public void printTransitions() {
    for (int i = 0; i < transitions.length; i++) {
      for (int j = 0; j < transitions[0].length; j++) {
        System.out.print(transitions[i][j]);
      }
      System.out.println("");
    }
  }
  
  public int runText(String text) { //see how many times pattern fits in text
    int timesFound = 0;
    int currentState = 0;
    for (int i = 0; i < text.length(); i++) {
      currentState = run(currentState, text.charAt(i));
      if (currentState == initialPattern.length) {
        timesFound++;
      }
    }
    return timesFound;
  }
}
