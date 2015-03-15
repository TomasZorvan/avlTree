package org.zorvan.avl;

import org.zorvan.avl.AVLNode;

/**
 * Class for testing purposes represents node in tree as a number
 * @author Tomas ZORVAN <tomas.zorvan at gmail.com>
 */
public class Int extends AVLNode<Int> {
    private final int value;

    public Int(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Int i) {
        if (this.getValue() < i.getValue()) {
            return -1;
        } else if (this.getValue() > i.getValue()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "" + this.getValue();
    }
}