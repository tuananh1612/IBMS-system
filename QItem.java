/* QItem.java
 * 
 * A class to hold an item of a priority queue
 * in our case the index of a node in the graph (bus stop index) 
 * and its priority (the current distance from source)
 * 
 * Last modified: Elise, 5 May
 * 
 * */
public class QItem {
  
  private int index;
  private int val;
  
  public QItem(int requiredIndex, int requiredVal) {
    index = requiredIndex;
    val = requiredVal;
  } // constructor
  
  public int getVal() {
    return val;
  } 
  
  public int getIndex() {
    return index;
  }
} // class
