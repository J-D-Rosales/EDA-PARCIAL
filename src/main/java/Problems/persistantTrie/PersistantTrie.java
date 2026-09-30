package Problems.persistantTrie;

public class PersistantTrie {
    TrieNode[] roots;
    private final int maxBits;

    public PersistantTrie(int maxBits, int maxElements) {
        this.roots = new TrieNode[maxElements + 2]; // Bounded in 1
        this.maxBits = maxBits;
    }

    public void insert(int version, int oldversion, long value) {
        // copiamos la version por la anterior
        this.roots[version] = new TrieNode(this.roots[oldversion]);

        // 2 punteros
        TrieNode currOld = this.roots[oldversion];
        TrieNode currNew = this.roots[version];

        for (int b = maxBits - 1; b >= 0; b--) {
            // bit mas significativo
            int bit = (int) ((value >> b) & 1);

            // Vemos por que bit debemos recorrer
            TrieNode nextOld = (currOld != null) ? currOld.children[bit] : null;
            TrieNode nextNew = new TrieNode(nextOld);

            currNew.children[bit] = nextNew;

            currOld = nextOld;
            currNew = nextNew;
        }
    }

    public long queryMaxXor(long Pi, int index){
        // Obtenemos el root correcto
        TrieNode root = this.roots[index+1];

        long result = 0;

        for (int b = maxBits - 1; b >= 0; b--) {
            // bit mas significativo
            int bit = (int) ((Pi >> b) & 1);
            int oppositeBit = 1-bit;

            if (root.children[oppositeBit] != null) {
                result |= 1L << b;
                root = root.children[oppositeBit];
            }
            else{
                root = root.children[bit];
            }
        }
        return result;
    }

}
