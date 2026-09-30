package Problems.persistanteTrieString;

import Problems.persistantTrie.TrieNode;

public class PersistantTrieS {
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
