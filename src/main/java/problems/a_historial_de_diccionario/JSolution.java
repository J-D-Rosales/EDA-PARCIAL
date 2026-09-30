package problems.a_historial_de_diccionario;

import Problems.persistanteTrieString.PersistantTrieS;
import Problems.persistanteTrieString.TrieNodeS;

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

        // ESCRIBE TU LÓGICA AQUÍ

        int q = sc.nextInt();
        PersistantTrieS trie = new PersistantTrieS(q);

        for (int  i = 1; i <= q; i++) {
            int op =  sc.nextInt();

            if (op == 1) {
                String word = sc.next();
                trie.insert(i,i-1,word);
            }
            else if (op == 2) {
                int version = sc.nextInt();
                trie.tOperation(version,i);
            }
            else if (op == 3) {
                String word = sc.next();
                trie.tOperation(i - 1, i);
                int result = trie.query(i,word);
                out.println(result);
            }
        }

        out.flush();
        out.close();
    }
}