# Generic AVL Tree

In computer science, an **AVL tree** (Georgy Adelson-Velsky and Landis' tree, named after the inventors) is a **self-balancing binary search tree**. 
It was the first such data structure to be invented. In an AVL tree, the heights of the two child subtrees of any node differ by at most one; 
if at any time they differ by more than one, **rebalancing** is done to restore this property. 
Lookup, insertion, and deletion all take O(log n) time in both the average and worst cases, where n is the number of nodes in the tree prior to the operation. 
Insertions and deletions may require the tree to be rebalanced by one or more tree rotations.

The AVL tree is named after its two Soviet inventors, Georgy Adelson-Velsky and E. M. Landis, who published it in their 1962 paper *"An algorithm for the organization of information"*.

AVL trees are often compared with **red-black trees** because both support the same set of operations and take O(log n) time for the basic operations. 
For *lookup-intensive applications, AVL trees are faster* than red-black trees because they are more rigidly balanced.
Similar to red-black trees, AVL trees are height-balanced.[[1]]

[Here][2] is very nice Java Applet showing also how balancing is working which is crucial to understand the concept of AVL tree.

## Implementation

There are two classes:

* AVLNode.java
* AVLTree.java

### Class AVLNode
AVLNode represents generic node of the AVL tree. If you want to create your own custom node that will be working fine with the AVL tree you need to extend this class the following way:

    ``` java
        public class MyAwesomeCustomNode extends AVLNode<MyAwesomeCustomNode> {

            @Override
            public int compareTo(MyAwesomeCustomNode o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String toString() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public int getValue() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    ```

You need to override all abstract methods. Method getValue() is present only for testing purposes. Pay attention especially to **compareTo** method where object comparison needs to be done.
To be able to insert node to AVL tree each object must have some kind of unique identifier which can be compared so that the decision if node is inserted in left or right subtree can be made.

See example below:

    ``` java
        public class Customer extends AVLNode<Customer> {
            private long idNumber;
            
            @Override
            public int compareTo(Customer c) {
                if (this.getIdNumber() < c.getIdNumber()) {
                    return -1;
                } else if (this.getIdNumber() > c.getIdNumber()) {
                    return 1;
                } else {
                    return 0;
                }
            }
            
            public long getIdNumber() {
                return idNumber;
            }
        }
    ```

### Class AVLTree
This class represents standalone AVLTree as a data structure. All necessary operations can be found here for further modification.

These operations are:

* Node insertion to tree
* Node deletion from tree
* Searching for specific node in tree
* Traversals:
    * Inorder - returns nodes in ascending order
    * Levelorder - returns nodes in levels as they appear in tree, also called breadth-first search

[1]:http://en.wikipedia.org/wiki/AVL_tree
[2]:http://www.site.uottawa.ca/~stan/csi2514/applets/avl/BT.html