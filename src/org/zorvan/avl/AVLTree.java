package org.zorvan.avl;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Class represents standalone AVL tree with necessary functionality.
 * @author Tomas ZORVAN <tomas.zorvan at gmail.com>
 */
public class AVLTree {
    private AVLNode root;
    
    /**
     * Constructor of the AVLTree class 
     */
    public AVLTree() {
        this.root = null; //root of the tree
    }
    
    /**
     * Method for search of particular node in the tree
     * @param node - wanted node
     * @return found node or null if node is not present in the tree
     */
    public AVLNode search(AVLNode node) {
        AVLNode activeNode = this.root;
        
        //if the root is null there is no tree to search
        if (this.root == null) {
            return null;
        }
        
        while (activeNode != null) {
            if (node.compareTo(activeNode) == 0) {
                //wanted node found
                return activeNode;
            } else if (activeNode.compareTo(node) <= -1) {
                activeNode = activeNode.getRight();
            } else {
                activeNode = activeNode.getLeft();
            }
        }
        return null;
    }
    
    /**
     * Insertion of the node to the tree
     * @param node - node to be inserted
     * @return true - if node was inserted, false - if node was not inserted
     */
    public boolean insert(AVLNode node) {
        //if there is no root the inserted node becomes root of the tree
        if (this.root == null) {
            this.root = node;
            return true;
        } else {
            AVLNode activeNode = this.root;
            
            while (true) {
                if (node.compareTo(activeNode) == 0) {
                    return false; //node alredy exists in the tree
                } else if (node.compareTo(activeNode) >= 1) {
                    //going to right subtree
                    if (activeNode.getRight() == null) {
                        activeNode.setRight(node);
                        node.setParent(activeNode);
                        
                        //reset node heights, detect rotation and get next node
                        this.resetHeightDetectRotation(node);
                        
                        return true;
                    } else {
                        activeNode = activeNode.getRight();
                    }
                } else {
                    //going to left subtree
                    if (activeNode.getLeft() == null) {
                        activeNode.setLeft(node);
                        node.setParent(activeNode);
                        
                        //reset node heights, detect rotation and get next node
                        this.resetHeightDetectRotation(node);
                        
                        return true;
                    } else {
                        activeNode = activeNode.getLeft();
                    }
                }
            }
        }
    }
    
    /**
     * Deletion of the node from the tree
     * @param node - node to be deleted
     * @return true - if the node was deleted, false - if the node was not deleted or error occurs 
     */
    public boolean delete(AVLNode node) {
        AVLNode nodeToRemove = this.search(node); //find out if deleting node is present in the tree
        AVLNode nodeToRemoveParent; //parent of the deleting node
        
        if (nodeToRemove != null) {
            //if deleting node is leaf = does not have any ancestors
            if (nodeToRemove.getLeft() == null && nodeToRemove.getRight() == null) {
                //if deleting node is not root
                if (nodeToRemove != this.root) {
                    nodeToRemoveParent = nodeToRemove.getParent();
                    //find out if deleting node is left or right son and delete it
                    if (nodeToRemoveParent.getLeft() == nodeToRemove) {
                        nodeToRemoveParent.setLeft(null);
                    } else {
                        nodeToRemoveParent.setRight(null);
                    }
                    //delete parent of the deleting node, no reference left for this node
                    nodeToRemove.setParent(null);
                    //reset node heights, detect rotation and get next node
                    this.resetHeightDetectRotation(nodeToRemoveParent);
                } else {
                    this.root = null;
                }
            //if deleting node has only one left son and will be replaced by him
            } else if(nodeToRemove.getLeftHeight() == 1 && nodeToRemove.getRightHeight() == 0) {
                AVLNode nodeToRemoveLeftChild = nodeToRemove.getLeft(); //left son of deleting node
                nodeToRemoveParent = nodeToRemove.getParent();
                //if deleting node is not root
                if (nodeToRemove != this.root) {
                    if (nodeToRemoveParent.getLeft() == nodeToRemove) {
                        nodeToRemoveParent.setLeft(nodeToRemoveLeftChild);
                    } else {
                        nodeToRemoveParent.setRight(nodeToRemoveLeftChild);
                    }
                    //set new parent to left son of the deleting node
                    nodeToRemoveLeftChild.setParent(nodeToRemoveParent);
                    //reset node heights, detect rotation and get next node
                    this.resetHeightDetectRotation(nodeToRemove.getLeft());
                } else {
                    //set new parent to left son of the deleting node
                    nodeToRemoveLeftChild.setParent(nodeToRemoveParent);
                    //left son becomes new root
                    this.root = nodeToRemove.getLeft();
                }
                nodeToRemove.setLeft(null); //remove left son of the deleting node
                nodeToRemove.setParent(null); //remove parent of the deleting node
            //if deleting node has only one right son and will be replaced by him
            } else if (nodeToRemove.getLeftHeight() == 0 && nodeToRemove.getRightHeight() == 1) {
                AVLNode nodeToRemoveRightChild = nodeToRemove.getRight(); //right son of deleting node
                nodeToRemoveParent = nodeToRemove.getParent();
                //if deleting node is not root
                if (nodeToRemove != this.root) {
                    if (nodeToRemoveParent.getLeft() == nodeToRemove) {
                        nodeToRemoveParent.setLeft(nodeToRemoveRightChild);
                    } else {
                        nodeToRemoveParent.setRight(nodeToRemoveRightChild);
                    }
                    //set new parent to right son of the deleting node
                    nodeToRemoveRightChild.setParent(nodeToRemoveParent);
                    //reset node heights, detect rotation and get next node
                    this.resetHeightDetectRotation(nodeToRemove.getRight());
                } else {
                    //set new parent to right son of the deleting node
                    nodeToRemoveRightChild.setParent(nodeToRemoveParent);
                    //right son becomes new root
                    this.root = nodeToRemoveRightChild;
                }
                nodeToRemove.setRight(null); //remove right son of the deleting node
                nodeToRemove.setParent(null); //remove parent of the deleting node
            //if deleting node has both right and left ancestors
            } else {
                nodeToRemoveParent = nodeToRemove.getParent();
                //find substitute for deleting node
                AVLNode substitute = this.getSubstitute(nodeToRemove);
                AVLNode tempNode;
                
                //if deleting node is parent of the substitute node
                if (nodeToRemove == substitute.getParent()) {
                    tempNode = substitute;
                } else {
                    tempNode = substitute.getParent();
                }
                
                //according to the substitute node is left or right son the references are set
                if (substitute.getParent().getLeft() == substitute) {
                    if (substitute.getRight() != null) {
                        substitute.getParent().setLeft(substitute.getRight()); //set substitute right son as the left son of the substitute parent
                        substitute.getRight().setParent(substitute.getParent()); //set new parent to the right node of the substitute node
                    } else {
                        substitute.getParent().setLeft(null);
                    }
                    substitute.setParent(null);
                } else {
                    if (substitute.getLeft() != null) {
                        substitute.getParent().setRight(substitute.getLeft());
                        substitute.getLeft().setParent(substitute.getParent());
                    } else {
                        substitute.getParent().setRight(null);
                    }
                    substitute.setParent(null);
                }
                
                //if parent of the deleting node is not root
                if (nodeToRemoveParent != null) {
                    substitute.setParent(nodeToRemoveParent); //set new parent to the substitute node
                    //find out if the deleting node is left or right son and set new parent to the substitute node
                    if (nodeToRemoveParent.getLeft() == nodeToRemove) {
                        nodeToRemoveParent.setLeft(substitute);
                    } else {
                        nodeToRemoveParent.setRight(substitute);
                    }
                } else {
                    //else substitute becomes new root
                    this.root = substitute;
                }
                
                //if deleting node has left son
                if (nodeToRemove.getLeft() != null) {
                    substitute.setLeft(nodeToRemove.getLeft());
                    nodeToRemove.getLeft().setParent(substitute);
                }
                //if deleting node has right son
                if (nodeToRemove.getRight() != null) {
                    substitute.setRight(nodeToRemove.getRight());
                    nodeToRemove.getRight().setParent(substitute);
                }
                
                //reset node heights, detect rotation and get next node
                this.resetHeightDetectRotation(tempNode);
                
                //standalone deletion of the node
                nodeToRemove.setParent(null);
                nodeToRemove.setLeft(null);
                nodeToRemove.setRight(null);
            }
        } else {
            //deleting node is not present in tree
            return false;
        }
        return true;
    }
    
    /**
     * Method for rotation detection
     * If any rotation needs to be done it will be performed
     * @param node - node on which rotation needs to be detected
     */
    private void detectRotation(AVLNode node) {
        //height of the right subtree is greater than height of the left subtree
        if (node.nodeBalanceFactor() >= 2) {
            //if the left height of the right subtree is greater than right height in the right subtree
            if (node.getRight().getLeftHeight() > node.getRight().getRightHeight()) {
                //right-left rotation is performed
                this.rightLeftRotation(node);
            } else {
                //left rotation is performed
                this.leftRotation(node);
            }
        }
        //height of the left subtree is greater than height of the right subtree
        if (node.nodeBalanceFactor() <= -2) {
            //if the right height of the left subtree is greater than left height of the left subtree
            if (node.getLeft().getRightHeight() > node.getLeft().getLeftHeight()) {
                //left-right rotation is performed
                this.leftRightRotation(node);
            } else {
                //right rotation is performed
                this.rightRotation(node);
            }
        }
    }
    
    /**
     * Method for setting or resetting heights and detecting possible rotations
     * @param node - node on which heights are set and rotations are detected
     */
    private void resetHeightDetectRotation(AVLNode node) {
        //until node is root; tracing back from inserted/deleted node to root
        while (node != null) {
            //setting new heights to nodes on the way to root
            node.setNodeHeights();
            //check if any rotation needs to be performed
            this.detectRotation(node);
            //return another node in the way to root node
            node = node.getParent();
        }
    }
    
    /**
     * Left rotation method on the given node
     * a                  b
     *  \               /   \
     *    b     =>     a     c   
     *     \
     *      c
     * 
     * @param node - node on which rotation is performed
     */
    private void leftRotation(AVLNode node) {
        AVLNode rightNode = node.getRight(); //right node of the rotating node
        AVLNode parentNode = node.getParent(); //parent of the rotating node
        //if parent of the rotating node is null then the rotating node is root
        if (parentNode == null) {
            rightNode.setParent(null);
            this.root = rightNode;
        } else if (parentNode.getRight() == node) {   
            //rotating node is right son
            parentNode.setRight(rightNode);
        } else {                          
            //rotating node is left son
            parentNode.setLeft(rightNode);
        }
        //right son of the rotating node becomes a new parent of the rotating node
        rightNode.setParent(node.getParent());
        //rotating node becomes left son of the rightNode node
        node.setParent(rightNode);
        
        //if right son of the rotating node does not have left son
        if (rightNode.getLeft() == null) {
            //setting right son of the rotating node to null
            node.setRight(null);
            //and set this node as left son of the right son of the rotating node 
            rightNode.setLeft(node);
        } else {
            rightNode.getLeft().setParent(node);
            node.setRight(rightNode.getLeft());
            rightNode.setLeft(node);
        }
        //set new heights to nodes
        node.setNodeHeights();
        node.getParent().setNodeHeights();
    }
    
    /**
     * Right rotation method on the given node
     *      a                b
     *     /               /   \
     *    b        =>     a     c 
     *   /
     *  c
     * 
     * @param node - node on which rotation is performed
     */
    private void rightRotation(AVLNode node) {
        AVLNode leftNode = node.getLeft(); //left node of the rotating node
        AVLNode parentNode = node.getParent(); //parent of the rotating node
        if (parentNode == null) {
            //if parent of the rotating node is null then the rotating node is root
            leftNode.setParent(null);
            this.root = leftNode;
        } else if (parentNode.getLeft() == node) {   
            //rotating node is right son
            parentNode.setLeft(leftNode);
        } else {                          
            //rotating node is left son
            parentNode.setRight(leftNode);
        }
        //left son of the rotating node becomes parent of rotating node
        leftNode.setParent(node.getParent());
        //rotating node becomes right son of the leftNode node
        node.setParent(leftNode);
        
        //if left son of the rotating node does not have right son
        if (leftNode.getRight() == null) {
            //setting left son of the rotating node to null
            node.setLeft(null);
            //and set this node as right son of the left son of the rotating node 
            leftNode.setRight(node);
        } else {
            leftNode.getRight().setParent(node);
            node.setLeft(leftNode.getRight());
            leftNode.setRight(node);
        }
        //set new heights to nodes
        node.setNodeHeights();
        node.getParent().setNodeHeights();
    }
    
    /**
     * Left-Right rotation method on the given node
     *    c            c
     *   /            /
     *  a     =>     b     =>     b 
     *   \          /           /   \
     *    b        a           a     c
     * 
     * @param node - node on which rotation is performed
     */
    private void leftRightRotation(AVLNode node) {
        //complete left rotation on the left node of the rotating node
        this.leftRotation(node.getLeft());
        //right rotation on the rotating node
        this.rightRotation(node);
    }
    
    /**
     * Right-Left rotation method on the given node
     * a           a
     *  \           \
     *   c     =>    b    =>     b
     *  /             \        /   \
     * b               c      a     c
     * 
     * @param node - node on which rotation is performed
     */
    private void rightLeftRotation(AVLNode node) {
        ///complete right rotation on the right node of the rotating node
        this.rightRotation(node.getRight());
        //left rotation on the rotating node
        this.leftRotation(node);
    }
    
    public AVLNode getRoot() {
        return this.root;
    }
    
    /**
     * Method to find substitute node for deleted node
     * We are looking for leftmost node from the right subtree or rightmost node from the left subtree
     * @param node - node to get substitute for
     * @return AVLNode - substitute
     */
    private AVLNode getSubstitute(AVLNode node) {
        AVLNode tempNode;
        //if there is not right subtree search the left subtree
        if (node.getRight() == null) {
            tempNode = node.getLeft(); //take left son
            while (true) {
                //while there is right subtree traverse it to the last node
                if (tempNode.getRight() != null) {
                    tempNode = tempNode.getRight();
                } else {
                    //else break from the loop
                    break;
                }
            }
            //and return substitute node
            return tempNode;
        } else {
            //same as above but in the right subtree
            tempNode = node.getRight();
            while (true) {
                if (tempNode.getLeft() != null) {
                    tempNode = tempNode.getLeft();
                } else {
                    break;
                }
            }
            return tempNode;
        }
    }
    
    /**
     * Inorder traversal from given node
     * @param node - node from which traversal begins
     * @return LinkedList - list of nodes
     */
    public LinkedList inorder(AVLNode node) {
        //to keep the nodes in the path that are waiting to be visited
        Stack s = new Stack();
        LinkedList<AVLNode> list = new LinkedList<>();
        //the first node to be visited is the leftmost
        while (node != null) {
            s.push(node);
            node = node.getLeft();
        }
        //traverse the tree
        while (s.size() > 0) {
            //visit the top node
            node = (AVLNode)s.pop();
            list.add(node);
            //find the next node
            if (node.getRight() != null) {
                node = node.getRight();
                //the next node to be visited is the leftmost
                while (node != null) {
                    s.push(node);
                    node = node.getLeft();
                }
            }
        }
        
        return list;
    }
    
    /**
     * Levelorder traversal from given node
     * @param node - node from which traversal begins
     * @return LinkedList - list of nodes
     */
    public LinkedList levelorder(AVLNode node) {
        LinkedList<AVLNode> levelorder = new LinkedList<>();
        LinkedList<AVLNode> list = new LinkedList<>();
        
        if (node != null) {
            levelorder.add(node);
        }
        
        while (!levelorder.isEmpty()) {
            AVLNode next = levelorder.remove();
            list.add(next);
            if (next.getLeft() != null) {
                levelorder.add(next.getLeft());
            }

            if (next.getRight() != null) {
                levelorder.add(next.getRight());
            }
        }
        
        return list;
    }
    
    /**
     * Method that counts number of nodes in tree
     * @param root - root node
     * @return int number of nodes in tree
     */
    public int getNumberOfNodes(AVLNode root) {
        if(root == null) {
            return 0;
        } else {
            return 1 + this.getNumberOfNodes(root.getLeft()) + this.getNumberOfNodes(root.getRight());
        }
    }
}