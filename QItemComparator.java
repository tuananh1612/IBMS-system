import java.util.Comparator;

/* QItemComparator.java
 * 
 * A class to tell our little priority queue 
 * what to base prioritization on.
 * 
 * In our case, the PQ has consists of QItems,
 * which store an index and a value.
 * We want to prioritize according to the value.
 * 
 * Last modified: Elise, 5 May
 * 
 * */
public class QItemComparator implements Comparator<QItem> {
  @Override
  public int compare(QItem x, QItem y) {
    if (x.getVal() < y.getVal())
      return -1;
    if (x.getVal() > y.getVal())
      return 1;
    return 0;
  } // compare
} // class
