package org.zorvan.avl;

import org.zorvan.avl.AVLNode;
import org.zorvan.avl.AVLTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Tomas ZORVAN <tomas.zorvan at gmail.com>
 */
public class TestAvlTree {
    private AVLTree avlTree;
    
    @Before
    public void setUp() {
        this.avlTree = new AVLTree();
    }
    
     @Test
     public void testInsertOneNode() {
         Int node1 = new Int(1);
         this.avlTree.insert(node1);
         assert !(node1.nodeBalanceFactor() >= 2) || !(node1.nodeBalanceFactor() <= -2);
         assert this.avlTree.getNumberOfNodes(this.avlTree.getRoot()) == 1;
     }
     
     @Test
     public void testInsert100Nodes() {
         boolean operationState;
         
         for (int i = 0; i < 100; i++) {
             Int nodeToInsert = new Int(i);
             operationState = avlTree.insert(nodeToInsert);
             assert operationState == true;
             assert !(nodeToInsert.nodeBalanceFactor() >= 2) || !(nodeToInsert.nodeBalanceFactor() <= -2);
         }
         
         assert this.avlTree.getNumberOfNodes(this.avlTree.getRoot()) == 100;
     }
     
     @Test
     public void testSearchFound() {
         Int node1 = new Int(1);
         this.avlTree.insert(node1);
         AVLNode foundNode = this.avlTree.search(node1);
         Assert.assertNotNull(foundNode);
     }
     
     @Test
     public void testSearchNotFound() {
         Int node1 = new Int(1);
         Int node2 = new Int(2);
         this.avlTree.insert(node1);
         AVLNode notFoundNode = this.avlTree.search(node2);
         Assert.assertNull(notFoundNode);
     }
     
     @Test
     public void testDelete() {
         boolean insertState, deleteState;
         
         for (int i = 0; i < 100; i++) {
             Int nodeToInsert = new Int(i);
             insertState = avlTree.insert(nodeToInsert);
             assert insertState == true;
         }
         
         Assert.assertTrue(this.avlTree.delete(new Int(50)));
         Assert.assertTrue(this.avlTree.delete(new Int(9)));
         Assert.assertTrue(this.avlTree.delete(new Int(38)));
         Assert.assertTrue(this.avlTree.delete(new Int(97)));
         Assert.assertTrue(this.avlTree.delete(new Int(73)));
     }
}
