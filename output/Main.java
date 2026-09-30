import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
    static class FastScanner {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");

        String next() {
            while (!st.hasMoreTokens()) {
                try {
                    String line = br.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }
    }

    public static void main(String[] args) {
        FastScanner sc = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int n = sc.nextInt();
        int q = sc.nextInt();

        PersistantTrieS trie = new PersistantTrieS(n);
        for (int i = 1; i <= n; i++) {
            String word = sc.next();
            trie.insert(i, i - 1, word);
        }
        for (int k = 0; k < q; k++) {
            int l = sc.nextInt();
            int r = sc.nextInt();
            String p = sc.next();

            // Una resta, porque sabemos que si tenemsof las primeros y restamos con el otro sale el que necesitamos
            int ans = trie.query(r, p) - trie.query(l - 1, p);
            out.println(ans);
        }

        out.flush();
        out.close();
    }
}

class PersistantTrieS {
    TrieNodeS[] roots;

    public PersistantTrieS(int maxElements) {
        this.roots = new TrieNodeS[maxElements + 2]; // Bounded in 1
        this.roots[0] = new TrieNodeS();
    }

    public void insert(int version, int oldversion, String word) {
        // Copiamos la nueva version
        this.roots[version]= new TrieNodeS(this.roots[oldversion]);

        // Ir insertando
        TrieNodeS currOld = this.roots[oldversion];
        TrieNodeS currNew = this.roots[version];
        currNew.prefixCount = currOld.prefixCount + 1;

        // Para cada letra de la palabra
        for (char c : word.toCharArray()) {

            TrieNodeS nextOld = (currOld != null) ? currOld.children[c - 'a'] : null;
            TrieNodeS nextNew = new TrieNodeS(nextOld);
            nextNew.prefixCount = (nextOld != null ? nextOld.prefixCount : 0) + 1;

            currNew.children[c - 'a'] = nextNew;

            currOld = nextOld;
            currNew = nextNew;
        }
    }

    public void tOperation(int targetVersion, int currentVersion) {
        // Asignación simple O(1) de la raíz
        this.roots[currentVersion] = this.roots[targetVersion];
    }

    public int query(int version, String word){
        // Sacamos el root en la version requerida
        TrieNodeS root = this.roots[version];

        for (char c : word.toCharArray()) {

            if (root.children[c - 'a'] == null) {
                return 0;
            }
            root = root.children[c - 'a'];
        }
        return root.prefixCount;
    }

}

class TrieNode {
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

class TrieNodeS {
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

