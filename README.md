# fun_with_datastructures

- ## DNA sequence alignment
  This project is a project where a memoization dynamic programming algorithm is used to achieve efficient computation. When matching 2 DNA sequences there may be 3 types of character comparisons: a match, a mismatch, and a gap. A match and mismatch are self explanitory, but a gap is where you choose to remove one of the characters from the sequence to improve the score of the DNA match. matches receive 2 points,  mismatches -2 points, and gaps -1 points. these point value rules are used to build up the memoization cache composing the next value in the cache of the bordering 3 values (e.g. (x - 1, y), (x - 1, y - 1), & (x, y - 1)). Then the algorithm traces back through the cache maximizing the score to find the optimal DNA alignment. the SequenceAligner.java class contains the memoization aspect of this algorithm.
  

- ## knn image classifier
  This project is an image classfier that uses the MNIST dataset of handwritten digits to train itself to classify handwritten numeric values. It employes a K-nearest neighbor algorithm where the majority classification of the K most similar images are used to select the image's value. This particular algorithm uses euclidean distance of the raw pixel intensities for differentiating images and it uses a heap for efficiency.

- ## palatte similarity

- ## permutations

- ## twitter bot project

- ## wire routing project
