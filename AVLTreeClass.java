import java.util.*;
import java.util.stream.Collectors;


public class AVLTreeClass {
    public static void main(String[] args) {
        AVLTreeClass avl = new AVLTreeClass();
        int[] arr = {3, 34, 1, 6, 9, 4, 2, 6, 81, 0, 11, 99, 100, 23, 5};
        List<Integer> nums = Arrays.stream(arr).boxed().collect(Collectors.toList());
        AVLTree<Integer, Integer> tree = new AVLTree<>();
        for (int n : nums) {
            tree.insert(n, 0);
            tree.test();
        }
        Collections.shuffle(nums);
        for (int n : nums) {
            tree.delete(n);
            tree.test();
        }

//        System.out.println(Arrays.toString(avl.sortArray(nums)));
    }


    public int[] sortArray(int[] nums) {
        AVLTree<Integer, Integer> tree = new AVLTree<>();
        for (int n : nums) {
            tree.insert(n, 0);
        }
        List<Integer> res = new ArrayList<>();
        for (int n : tree) {
            res.add(n);
        }
        int[] array = new int[res.size()];
        int count = 0;
        for (int n : res) {
            array[count++] = n;
        }
        return array;

    }
}

class AVLTree<K extends Comparable<K>, V> implements Iterable<K> {
    private Node root = null;

    @Override
    public Iterator<K> iterator() {
        return new InorderIterator(root);
    }

    private int height(Node root) {
        return root == null ? 0 : root.height;
    }

    private Node rotateLeft(Node root) {
        Node nr = root.right;
        Node tmp = nr.left;
        nr.left = root;
        root.right = tmp;
        root.height = 1 + Math.max(height(root.left), height(root.right));
        nr.height = 1 + Math.max(height(nr.left), height(nr.right));
        return nr;
    }

    private Node rotateRight(Node root) {
        Node newRoot = root.left;
        Node tmp = newRoot.right;
        newRoot.right = root;
        root.left = tmp;
        root.height = 1 + Math.max(height(root.left), height(root.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));
        return newRoot;
    }

    private int balanceFactor(Node root) {
        // if balance factor > 1 or < -1 we need to balance
        return height(root.left) - height(root.right);
    }

    private Node balance(Node root) {
        int bf = balanceFactor(root);
        if (bf > 1) {
            if (balanceFactor(root.left) < 0) {
                root.left = rotateLeft(root.left);
            }
            root = rotateRight(root);
        } else if (bf < -1) {
            if (balanceFactor(root.right) > 0) {
                root.right = rotateRight(root.right);
            }
            root = rotateLeft(root);
        }
        return root;
    }

    public void insert(K k, V v) {
        root = insert(root, new Node(k, v));
    }

    private Node insert(Node root, Node toInsert) {
        if (root == null) return toInsert;
        int comp = toInsert.key.compareTo(root.key);
        if (comp < 0) {
            root.left = insert(root.left, toInsert);
        } else if (comp > 0) {
            root.right = insert(root.right, toInsert);
        } else {
            root.val = toInsert.val;
        }
        root.height = 1 + Math.max(height(root.left), height(root.right));

        return balance(root);
    }

    public V find(K k) {
        Node node = find(root, k);
        return node == null ? null : node.val;
    }

    private Node find(Node root, K k) {
        if (root == null) return null;
        int cmp = root.key.compareTo(k);
        if (cmp == 0) return root;

        if (cmp > 0) {
            return find(root.left, k);
        }
        return find(root.right, k);

    }

    public void delete(K k) {
        root = delete(root, k);
    }

    private Node findMin(Node root) {
        if (root.left == null) {
            return root;
        }
        return findMin(root.left);
    }

    private Node findMax(Node root) {
        if (root.right == null) {
            return root;
        }
        return findMax(root.right);
    }

    private Node delete(Node root, K k) {
        if (root == null) {
            return null;
        }
        int cmp = k.compareTo(root.key);
        if (cmp < 0) {
            root.left = delete(root.left, k);
        } else if (cmp > 0) {
            root.right = delete(root.right, k);
        } else {
            if (root.left == null || root.right == null) {
                return root.left == null ? root.right : root.left;
            }
//            if (height(root.left) > height(root.right)) {
//                // take predecessor
//                Node predecessor = findMax(root.left);
//                predecessor.left = delete(root.left, predecessor.key);
//                predecessor.right = root.right;
//                root = predecessor;
//
//            } else {
                // take successor
                Node successor = findMin(root.right);
                root.right = delete(root.right, successor.key);
                successor.left = root.left;
                successor.right = root.right;
                root = successor;
//            }

        }
        root.height = 1 + Math.max(height(root.left), height(root.right));
        return balance(root);
    }

    public void test() {
        if (!isValidBST(root)) System.out.println("BST order invalid");
        if (!isBalanced(root)) System.out.println("Not balanced");
        if (!isValidHeight(root)) System.out.println("Not valid height");
    }

    private boolean isValidBST(Node root) {
        return isValid(root, null, null);
    }

    private boolean isValid(Node root, K lBound, K rBound) {
        if (root == null) return true;
        if ((rBound != null && root.key.compareTo(rBound) >= 0) || (lBound != null && root.key.compareTo(lBound) <= 0))
            return false;
        return isValid(root.left, lBound, root.key) && isValid(root.right, root.key, rBound);
    }


    private boolean isBalanced(Node root) {
        return balanced(root) != -1;
    }

    private int balanced(Node root) {
        if (root == null) return 0;
        int leftHeight = balanced(root.left);
        if (leftHeight == -1) {
            return -1;
        }
        int rightHeight = balanced(root.right);
        if (rightHeight == -1) return -1;
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }
        return 1 + Math.max(leftHeight, rightHeight);
    }

    private boolean isValidHeight(Node root) {
        return checkHeight(root) != -1;
    }

    private int checkHeight(Node root) {
        if (root == null) return 0;
        int leftHeight = checkHeight(root.left);
        if (leftHeight < 0) return -1;
        int rightHeight = checkHeight(root.right);
        if (rightHeight < 0) return -1;
        int h = 1 + Math.max(leftHeight, rightHeight);
        if (h != root.height) return -1;
        return h;
    }

    private class Node {
        private K key;
        private V val;
        private int height = 1;
        private Node left;
        private Node right;

        Node(K k, V v) {
            key = k;
            val = v;
        }
    }

    private class InorderIterator implements Iterator<K> {
        Deque<Node> stack = new ArrayDeque<>();

        InorderIterator(Node root) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            // before beginning every hasNext(), stack.top is the node to return
            Node res = stack.pop();
            // if there is a successor, put path to successor on stack
            Node right = res.right;
            while (right != null) {
                stack.push(right);
                right = right.left;
            }
            return res.key;
        }


    }


}
