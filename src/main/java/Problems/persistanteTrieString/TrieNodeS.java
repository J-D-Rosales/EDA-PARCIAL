package Problems.persistanteTrieString;

public class TrieNodeS {
    TrieNodeS[] children;
    int prefixCount;

    public TrieNodeS() {
        children = new TrieNodeS[26];
        prefixCount = 0;
    }

    public TrieNodeS(TrieNodeS old) {
        this.children = new TrieNodeS[26];
        if (old != null) {
            System.arraycopy(old.children, 0, this.children, 0, 26);
            this.prefixCount = old.prefixCount;
        }
        else {
            this.prefixCount = 0;
        }
    }

}