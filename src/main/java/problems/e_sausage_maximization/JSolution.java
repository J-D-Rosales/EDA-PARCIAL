package problems.e_sausage_maximization;

import Problems.persistantTrie.PersistantTrie;
import Problems.persistantTrie.TrieNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
        int numero_elementos = sc.nextInt();

        long[] array = new long[numero_elementos + 2]; // todos son ceros por defecto

        for (int i = 1; i <= numero_elementos; i++) {
            array[i] = sc.nextLong();
        }

        // Creamos los arreglos S y P, Como P va a ser indexado en 1
        // y además va ser de 0 a n+1, entonces el arreglo debe tener n+2

        long[] P = new long[numero_elementos+2];
        long[] S = new long[numero_elementos+2];

        // REllenamos P
        for (int i = 1; i <= numero_elementos; i++) {
            P[i] = P[i-1] ^ array[i]; // resultado xor elemento anterior
        }

        // Rellenamos S
        for (int j = numero_elementos; j >= 1; j--) {
            S[j] = S[j + 1] ^ array[j];
        }

        PersistantTrie trie = new PersistantTrie(41, numero_elementos);
        // Colocamos en el trie el S
        for (int i = numero_elementos+1; i >= 1; i--) {
            int oldVersion = (i == numero_elementos+1) ? 0 : i+1;
            trie.insert(i,oldVersion,S[i]);
        }

        // hacemos las consultas
        long resultado = 0;
        for (int i = 0; i <= numero_elementos; i++) {
            long resultado_temporal = trie.queryMaxXor(P[i],i);
            resultado = Math.max(resultado_temporal, resultado);
        }
        out.println(resultado);
        out.flush();
        out.close();
    }
}