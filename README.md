# fun_with_datastructures

- ## DNA sequence alignment
  This project is a project where a memoization dynamic programming algorithm is used to achieve efficient computation. When matching 2 DNA sequences there may be 3 types of character comparisons: a match, a mismatch, and a gap. A match and mismatch are self explanitory, but a gap is where you choose to remove one of the characters from the sequence to improve the score of the DNA match. matches receive 2 points,  mismatches -2 points, and gaps -1 points. these point value rules are used to build up the memoization cache composing the next value in the cache of the bordering 3 values (e.g. (x - 1, y), (x - 1, y - 1), & (x, y - 1)). Then the algorithm traces back through the cache maximizing the score to find the optimal DNA alignment. the SequenceAligner.java class contains the memoization aspect of this algorithm.
  

- ## knn image classifier
  This project is an image classfier that uses the MNIST dataset of handwritten digits to train itself to classify handwritten numeric values. It employes a K-nearest neighbor algorithm where the majority classification of the K most similar images are used to select the image's value. This particular algorithm uses euclidean distance of the raw pixel intensities for differentiating images and it uses a heap for efficiency.

- ## palatte similarity
  This project uses a hashmap to compare the RGB pixel colors in an image to that of another. This is done at various levels of quantization that can be modified by the sliding bar provided by the GUI anywhere from 8 bits per RGB channel all the way down to one. Additionally when you click and hold your cursor on a particular pixel on the screen it will highlight all of the other matching pixels on both images using the xray effect. The hash table itself has been implemented to be self-adjusting to move the previously fetched list item to the head of the list for each array index within the table, and it uses chaining as the collision resolution strategy.

- ## permutations

- ## twitter bot project
  This project was one of my favorites where Twitter's API was used in conjunction with string matching algorithms to efficiently search twitter data for a preselected string. In addition it contains the functionality to see what people have been tweeting about given a subject string, and can return the most popular words tweeted by someone with a given twitter handle. The string matching algorithms were a naive algorithm, Knuth-Morris-Pratt (KMP), and Boyer-Moore.  The niave algorithm has no optimizations. The KMP algorithm constructs a finite state automata of a search string to be found in other strings and has a worstcase linear run time. Finally the Boyer-Moore algorithm uses a nifty shift routine for substrings that are known not to match and the Galil rule which skips substring comparisons that are known to match creating a linear runtime in the worst case and sublinear in the majority of cases. In this project I found that KMP runs well on strings with a smaller alphabet, where Boyer-Moore surpasses KMP in key comparisons when the strings are longer and the alphabett size is larger. For example KMP is much better at comparing binary and hexidecimal strings, while Boyer-Moore is much better at comparing ASCII strings.

- ## wire routing project
  
