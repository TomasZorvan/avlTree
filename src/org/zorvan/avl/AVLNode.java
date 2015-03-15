package org.zorvan.avl;

/**
 * Abstract class that represents node of the AVL tree,
 * @param <T> - generic type of the AVL node
 * 
 * @author Tomas ZORVAN <tomas.zorvan at gmail.com>
 */
public abstract class AVLNode<T> {
    private AVLNode left, right, parent; //left, right and parent node of the particular node
    private int leftHeight, rightHeight; //left and right height of the node

    public AVLNode() {
        this.left = null;
        this.right = null;
        this.parent = null;
        this.leftHeight = 0;
        this.rightHeight = 0;
    }
    
    /**
     * Method that returns node balance factor as a difference between right and left node height
     * @return balance factor of the node
     */
    public int nodeBalanceFactor() {
        return (this.rightHeight - this.leftHeight);
    }
    
    /**
     * Method that returns absolute height of the node
     * @return absolute node height
     */
    public int nodeHeight() {
        return (this.rightHeight > this.leftHeight) ? this.rightHeight : this.leftHeight;
    }
    
    /**
     * Method to set node heights after rotation is performed
     */
    public void setNodeHeights() {
        if (this.right == null) {
            this.rightHeight = 0;
        } else {
            this.rightHeight = this.right.nodeHeight() + 1;
        }
        
        if (this.left == null) {
            this.leftHeight = 0;
        } else {
            this.leftHeight = this.left.nodeHeight() + 1;
        }
    }
    
    /**
     * Abstract method for object comparison.
     * @param o - compared object
     * @return int
     */
    public abstract int compareTo(T o);
    
    @Override
    public abstract String toString();
    
    /**
     * Just for testing purpose. Can be deleted but tests wont pass.
     * @return int
     */
    public abstract int getValue();
    
    //<editor-fold defaultstate="collapsed" desc="GETTERS & SETTERS">
    public AVLNode getLeft() {
        return left;
    }

    public void setLeft(AVLNode left) {
        this.left = left;
    }

    public AVLNode getRight() {
        return right;
    }

    public void setRight(AVLNode right) {
        this.right = right;
    }

    public AVLNode getParent() {
        return parent;
    }

    public void setParent(AVLNode parent) {
        this.parent = parent;
    }

    public int getLeftHeight() {
        return leftHeight;
    }

    public void setLeftHeight(int leftHeight) {
        this.leftHeight = leftHeight;
    }

    public int getRightHeight() {
        return rightHeight;
    }

    public void setRightHeight(int rightHeight) {
        this.rightHeight = rightHeight;
    }
    //</editor-fold>
}