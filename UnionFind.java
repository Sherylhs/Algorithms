class UF {
    private static class Node {
        private Node parent = this;
        private int id = 0;
        private int rank = 1;
        public Node(int id) {
            this.id = id;
        }
    }
    
    private Map<Integer, Node> nodes = new HashMap<>();
    private int count = 0; // island count
    public UF() {
        
    }
    
    public void add(int id) {
        // add node with id to UF
        if (nodes.containsKey(id)) return;
        nodes.put(id, new Node(id));
        count++;
    }
    
    public void connect(int p, int q) {
        // connect nodes p and q
        Node pRoot = find(nodes.get(p));
        Node qRoot = find(nodes.get(q));
        if (pRoot == qRoot) return;
        if (pRoot.rank > qRoot.rank) {
            qRoot.parent = pRoot;
        } else if (qRoot.rank > pRoot.rank) {
            pRoot.parent = qRoot;
        } else {
            qRoot.parent = pRoot;
            pRoot.rank++;
        }
        count--;
    }
    
    private Node find(Node n) {
        // find root of node for this id
        if (n.parent == n) {
            return n;
        }
        Node root = find(n.parent);
        n.parent = root;
        return root;
    }
    
    public int count() {
        return count;
    }
}
