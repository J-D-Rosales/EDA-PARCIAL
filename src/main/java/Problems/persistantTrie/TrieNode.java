package Problems.persistantTrie;

public class TrieNode {
    TrieNode[] children;

    public TrieNode() {
        children = new TrieNode[2];}

    public TrieNode(TrieNode old) {
        this.children = new TrieNode[2];
        if (old != null) {
            this.children[0] = old.children[0];
            this.children[1] = old.children[1];
        }
    }

}