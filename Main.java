import java.util.*;

public class Main {
  public static void main(String args[]) {
    AVL tree = new AVL();
    /*
     *             10  
     *         5 
     *     3
     *                9
     *             10  
     *         5       11
     *     3      6       15
     *   2   4      7
     *                9
     */
    tree.add(10);
    tree.add(5);
    tree.add(6);
    tree.add(2);
    tree.add(4);
    tree.add(6);
    tree.add(7);
    tree.add(9);
    tree.add(11);
    tree.add(15);
    System.out.println("Height : " + tree.height());
    tree.inorder(); // 2 3 4 5 6 7 9 10 11 15
    tree.remove(2);
    tree.inorder(); // 3 4 5 6 7 9 10 11 15
    tree.remove(5);
    tree.inorder(); // 3 4 6 7 9 10 11 15
  }
}
class AVL extends BinarySearchTree {
  public void add(int key) {
    System.out.println("Adding " + key);
    root = _add(root, key);
  }
  public void remove(int key) {
    if(search(key)) {
      root = _remove(root, key);
    } else {
      System.out.println("Not Found");
    }
  }
  private Node _add(Node root, int key) {
    if(root == null) { // Actually add node
      root = new Node(key);
    } else { // Recursively Add
      if(key > root.value) {
        root.right = _add(root.right, key);
      } else {
        root.left = _add(root.left, key); 
      }
    }

    // Check balance factor
    int leftHeight = _height(root.left);
    int rightHeight = _height(root.right);
    int balanceFactor = leftHeight - rightHeight;

    if(Math.abs(balanceFactor) > 1) { // Unbalanced tree
      if(leftHeight > rightHeight) { // Unbalanced at left
        if(balanceFactor * (_height(root.left.left) - _height(root.left.right)) < 0) { // Double LR Rotation
          rotateLeft(root.left);
        }
        rotateRight(root);
      } else { // Unbalanced at right
        if(balanceFactor * (_height(root.right.left) - _height(root.right.right)) < 0) { // Double RL Rotation
          rotateRight(root.right);
        }
        rotateLeft(root);
      }
      // Check balance factor
      leftHeight = _height(root.left);
      rightHeight = _height(root.right);
      balanceFactor = leftHeight - rightHeight;
    }

    return root;
  }
  private Node _remove(Node root, int key) {
    if(root == null) {
      return null;
    }
    if(key == root.value) {
      if(root.isLeaf()) {
        return null;
      } else if(root.hasLeft()) {
        root.value = root.left.value;
        root.left = null;
        return root;
      } else if(root.hasRight()) {
        root.value = root.right.value;
        root.right = null;
        return root;
      } else if(root.hasBothChildren()) {
        Node predecessor = root.getPredecessor();
        root.value = predecessor.value;
        root.left = _remove(root.left, predecessor.value);
        return root;
      }
    } else if(key < root.value) {
      root.left = _remove(root.left, key);
    } else if(key > root.value) {
      root.right = _remove(root.right, key);
    }
    return root;
  }
  private void rotateLeft(Node n) {
    Node newRoot = n.right;
    n.right = n.right.left;
    newRoot.left = n;
    n = newRoot;
  }
  private void rotateRight(Node n) {
    Node newRoot = n.left;
    n.left = n.left.right;
    newRoot.right = n;
    n = newRoot;
  }
}
class BinarySearchTree {
  public Node root;
  public void inorder() {
    _inorder(root);
    System.out.println();
  } 
  public void postorder() {
    _postorder(root);
    System.out.println();
  } 
  public void preorder() {
    _preorder(root);
    System.out.println();
  } 
  public boolean search(int key) {
    return _search(root, key);  
  }
  private boolean _search(Node root, int key) {
    if(root != null) {
      if(key > root.value) {
        return _search(root.right, key);
      } else if(key < root.value) {
        return _search(root.left, key);
      } else {
        return true;
      }
    } 
    return false;
  }
  private void _inorder(Node root) {
    if(root != null) {
      _inorder(root.left);
      System.out.print(root.value + " ");
      int leftHeight = _height(root.left);
      int rightHeight = _height(root.right);
      int balanceFactor = leftHeight - rightHeight;

      System.out.println("BF for " + root.value + " : " + leftHeight + " - " + rightHeight + " = " + balanceFactor);

      _inorder(root.right);
    }
  }
  private void _postorder(Node root) {
    if(root != null) {
      _postorder(root.left);
      _postorder(root.right);
      System.out.print(root.value + " ");
    }
  }
  private void _preorder(Node root) {
    if(root != null) {
      System.out.print(root.value + " ");
      _preorder(root.left);
      _preorder(root.right);
    }
  }
  public int height() {
    return root != null ? _height(root) : -1;
  }
  protected int _height(Node root) {
    return root == null ? 0 : ((Math.max(_height(root.left), _height(root.right))) + 1);
  }
}
class Node {
  public Node left;
  public Node right;
  public int value;

  Node (int v) {
    left = null;
    right = null;
    value = v;
  }
  public boolean isLeaf() {
    return left == null && right == null;
  }
  public boolean hasLeft() {
    return left != null && right == null;
  }
  public boolean hasRight() {
    return right != null && left == null;
  }
  public boolean hasBothChildren() {
    return right != null && left != null;
  }
  public Node getSuccessor() {
    Node temp = right;
    while(temp.left != null) {
      temp = temp.left;
    } 
    return temp;
  }
  public Node getPredecessor() {
    Node temp = left;
    while(temp.right != null) {
      temp = temp.right;
    } 
    return temp;
  }
}
