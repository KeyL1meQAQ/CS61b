public class Tries {

    class Node {
        private char letter;
        private Node[] nodes;
        private boolean isEnd = false;

        Node(char letter) {
            this.letter = letter;
            nodes = new Node[256];
        }

        Node() {
            nodes = new Node[256];
        }

        public void addNode(char c) {
            if (nodes[c] == null) {
                nodes[c] = new Node(c);
            }
        }

        public Node getNode(int index) {
            return nodes[index] == null ? null : nodes[index];
        }

        public void setEnd() {
            isEnd = true;
        }

        public char getLetter() {
            return letter;
        }

        public boolean isEnd() {
            return isEnd;
        }
    }

    private Node root;

    public Tries() {
        root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    public void addString(String s) {
        char[] chars = s.toCharArray();
        Node node = getRoot();
        for (int i = 0; i < chars.length; i++) {
            node.addNode(chars[i]);
            node = node.getNode(chars[i]);
        }
        node.setEnd();
    }
}
