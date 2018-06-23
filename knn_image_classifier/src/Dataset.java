import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Random;
import java.io.*;

public class Dataset extends LinkedList<Digit> {

  //private File trainingData = new File(Constants.TRAINING_DATA);
  private Dataset trainingData;//= new Dataset(Constants.TRAINING_DATA);
  private Digit[] neighbors;//= new Digit[trainingData.size()]; // will be filled with digits from dataset???
  private int position;

  /**
   * Creates an empty dataset.
   */

  public Dataset() {
    super();
    //System.out.println(trainingData.getCanonicalPath());
  }

  /**
   * Initializes this dataset by loading digits from the given file.
   */

  public Dataset(String filename) {
    load(filename);
    //neighbors = new Digit[this.size()];
  }


  /**
   * Runs knn on each entry in the given test set, using k as the number of
   * nearest neighbors and this dataset as the training set.
   */

  public void classify(int k, Dataset unknowns) {
    int i = 1;
    for (Digit d : unknowns) {
      d.classify(knn(k, d).getLabel());
      System.out.println(i++ + ", " + d.getLabel());
    }
  }

  /**
   * Searches this dataset to find the k nearest neighbors to the given
   * unknown, and then returns the closest neighbor with the majority label.
   * 
   * Assume k is a positive integer less than the size of this dataset.
   *
   *  **************** CLASS NOTES *********
   *
   *  •Calculate the distance between the unknown and an element in the training set exactly once.
   *  •Store distance informaJon in a heap.
   *  •Run heapifyexactly once.
   *  •Implement siftDownalgorithm iteratively to save space.
   *  •Run Phase II loop exactly k Jmes to idenJfy the neighbors.
   *
   *  *********** WABALUBADUBDUB ***********
   */

  /*public Digit knn(int k, Digit unknown) {
    assert k > 0;
    assert k < size();
    neighbors = new Digit[this.size()];
    position = 0;

    // find distance and add Digits to neighbors array
    for(Digit known: this) {
        neighbors[position] = known; //might only need to be filled once
        neighbors[position].setDistance(unknown.distance(known));
        position++;
    }
    // heapify to organize min heap
    heapify(neighbors);
    *//* find the (MAJORITY), "q.
     * *//*
    int last = neighbors.length - 1;
    Digit[] kDigits = new Digit[k];
    for(int i = 0; i < Constants.K; i++) {
        Digit hold = neighbors[0];
        neighbors[0] = neighbors[last];
        last--;
        siftDown(neighbors, 0, last);
        kDigits[i] = hold;
    }
         //////////***************≈
    *//*
     *  use CountKs to find the "Majority"
     *//*

    // read info into histogram array to compare counts of labels
    boolean found;
    int it = 0;
    CountKs[] histArray = new CountKs[k]; // at most could be length k
      histArray[it] = new CountKs(kDigits[0].getLabel());
      it++;
    for(int i = 1; i < kDigits.length; i++) {
        found = false;
        for(int j = 0; j <= i; j++) {
            if(histArray[j].getKey() == kDigits[i].getLabel()) {
                found = true;
                histArray[j].incrementCount();
                j = i + 1; // so we go to next i
            }
        }
        if(!found) {
            histArray[it] = new CountKs(kDigits[i].getLabel());
            it++;
        }
    }

    // determine majority label
      int max = 1, max_key = histArray[0].getKey();
    for(int i = 0; i <= it; i++) { // iter all unique keys
        if(histArray[i].getCount() > max) {
            max = histArray[i].getCount();
            max_key = histArray[i].getKey();
        }
    }

    for(int i = 0; i < it; i++) {
        if(kDigits[i].getLabel() == max_key)
            return kDigits[i];
    }


    return kDigits[0];
  }*/


    /**  NOTES:
     * - make sure it's a
     *
     */
  public Digit knn(int k, Digit unknown) {

      if (k < 1 || k > size())
          throw new IndexOutOfBoundsException();

      PriorityQueue<Digit> pq = new PriorityQueue<>((s, t) -> t.compareTo(s));
      for (int i = 0; i < size(); i++)
          pq.offer(this.get(i));

      // needs a little refactoring
      // don't return kth largest return kth nearest neighbor
      for (int i = 0; i < k - 1; i++)
          pq.poll();
      return pq.poll();

  }

  /********************************************************
     *
     * Rearranges the elements of a so that they form a max-heap.
     */

    /*public static void heapify(Digit[] a) {
        int n = a.length;
        int last = n - 1;

        int i = (int) Math.floor(n/2 - 1);

        while(i >= 0) {
            siftDown(a, i, last);
            //System.out.println(Integer.toString(i));
            i--;
        }
    }*/

    /**
     * Restores the ordering property at node p so that elements from
     * 0 to last, inclusive, in the array a form a max-heap.
     */

    /*public static void siftDown(Digit[] a, int p, int last) {
        int left = leftChild(p), right = rightChild(p),c = left, n = a.length/2;


        while(left <= last){
            System.out.print(Integer.toString(n) + ", " + Integer.toString(last) + ", " );
            if(right <= last && a[left].getDistance() < a[right].getDistance())
                c = right;
            if(a[p].getDistance() < a[c].getDistance()) {
                swap(a, p, right);
                p = c;
            }
            if(a[p].getDistance() < a[left].getDistance()) {
                swap(a, p, c);
                p = c;
                left = leftChild(p);
                right = rightChild(p);
                c = left;
            }
            else {
                return;
            }
        }

    }*/




    /**
     * Exchanges the elements at indices i and j in the array a.
     */
    public static void swap(Digit[] a, int i, int j) {
        Digit temp = a[i]; // will it switch right since only passing obj refs?
        a[i] = a[j];
        a[j] = temp;

        return;
    }



    public static int leftChild(int p) {
        return 2 * p + 1;
    }



    public static int rightChild(int p) {
        return leftChild(p) + 1;
    }



    public static int parent(int p) {
        return (p - 1) / 2;
    }

  /********************************************************/

  /**
   * Returns the array of nearest neighbors found by the most
   * recent call to knn.
   */

  public Digit[] getNeighbors() {
    return neighbors;
  }

  /**
   * Loads the digits from the given filename into this dataset.
   */

  public void load(String filename) {
    if (!filename.endsWith(".csv"))
      filename += ".csv";
    try {
      BufferedReader in = new BufferedReader(new FileReader(new File(filename)));
      in.readLine(); // get rid of header
      in.lines().forEachOrdered(line -> add(new Digit(line)));
      in.close();
    }
    catch (IOException ex) {
      System.out.println("Problem reading file: [" + filename + "]\n");
    }    
  }

  /**
   * Saves this dataset into the specified file using the MNIST format.
   */

  public void save(String filename) {
    if (!filename.endsWith(".csv"))
      filename += ".csv";
    StringBuilder header = new StringBuilder();
    header.append("label,");
    int last = Constants.DIM * Constants.DIM - 1;
    for (int i = 0; i < last - 1; i++)
      header.append("pixel").append(i).append(",");
    header.append("pixel").append(last);  
    try {
      PrintWriter out = new PrintWriter(new File(filename));
      out.println(header);
      for (Digit d : this) 
        out.println(d.getCode());
      out.close();
    }
    catch (IOException e) {
      System.out.println("Problem creating the file: [" + filename + "]\n.");
    } 
  }

  /**
   * Returns the accuracy of knn on m random digits in this dataset. Takes a
   * long time if this dataset is large.
   */

  public double validate(int k, int m) {
    int n = size(), matches = 0;
    Random rand = new Random();
    for (int i = 0; i < m; i++) {
      int pos = rand.nextInt(n);
      // Select a sample and remove it (temporarily) from this dataset to avoid
      // matching with itself.
      Digit sample = remove(pos);
      Digit match = knn(k, sample);
      if (sample.getLabel() == match.getLabel()) {
        matches++;
        System.out.print(".");
      }
      else
        System.out.print("x");
      add(pos, sample);
    }
    System.out.println();
    return (100.0 * matches) / m;
  }

  /**
   * Simple testing.
   */

  public static void main(String[] args) {
    Dataset tiny = new Dataset();
    System.out.println("reading dataset");
    Dataset train = new Dataset(Constants.DATA_DIR + "/menzel.csv");
    System.out.println("dataset instantiated");
    train.classify(3, tiny);
    System.out.println("\nSize of dataset: " + train.size());
    int m = 50;
    for (int k = 3; k <= 11; k += 2, m += 10)
      System.out.format("k = %2d: %.1f%%%n%n", k, train.validate(k, m));
  }

}