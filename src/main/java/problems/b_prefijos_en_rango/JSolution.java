package problems.b_prefijos_en_rango;

import Problems.persistanteTrieString.PersistantTrieS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class JSolution {
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

        // 1. Instanciamos el Trie Persistente para N palabras
        PersistantTrieS trie = new PersistantTrieS(n);

        // 2. Insertamos la secuencia de palabras de s_1 a s_n
        for (int i = 1; i <= n; i++) {
            String word = sc.next();
            trie.insert(i, i - 1, word);
        }

        // 3. Respondemos las Q consultas en rango [l, r]
        for (int k = 0; k < q; k++) {
            int l = sc.nextInt();
            int r = sc.nextInt();
            String p = sc.next();

            int ans = trie.query(r, p) - trie.query(l - 1, p);
            out.println(ans);
        }

        out.flush();
        out.close();
    }
}