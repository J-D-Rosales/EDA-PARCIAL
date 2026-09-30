package org.example;

import Problems.*;
import Problems.hollowHeap.HollowHeap;
import Problems.hollowHeap.HollowItem;
import Problems.rangeTree.Pointd;
import Problems.rangeTree.RangeTree;
import Problems.splayTree.SplayTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {

    public void problema1() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();

        if (line == null) return;

        int n = Integer.parseInt(line.trim());


        StackPersistence stack = new StackPersistence();
        long totalMass = 0;

        for (int i = 1; i <= n; i++) {
            line = reader.readLine();
            StringTokenizer tokenizer = new StringTokenizer(line);

            int t = Integer.parseInt(tokenizer.nextToken());
            int m = Integer.parseInt(tokenizer.nextToken());

            int newVersion;
            if (m > 0) {
                newVersion = stack.push(m,t);
            } else {
                newVersion = stack.pop(t);
            }
            totalMass += stack.getVersionTotalMass(newVersion);
        }
        System.out.println(totalMass);
    }

    public static void problema2() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = reader.readLine();
        if (line == null) return;

        int n = Integer.parseInt(line.trim());
        int[] array = new int[n];

        line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line);

        for (int i = 0; i < n; i++) {
            array[i] = Integer.parseInt(tokenizer.nextToken());
        }

        SegmentTreePersistance persistance = new SegmentTreePersistance(array);

        // Usamos StringBuilder para la salida rápida en Java
        StringBuilder sb = new StringBuilder();

        line = reader.readLine();
        if (line == null) return;
        int m = Integer.parseInt(line.trim());

        for (int i = 0; i < m; i++) {
            line = reader.readLine();
            if (line == null) break;
            tokenizer = new StringTokenizer(line);
            String texto = tokenizer.nextToken();

            if (texto.equals("create")) {
                int version = Integer.parseInt(tokenizer.nextToken()) - 1;  // Ajuste Base 0
                int posicion = Integer.parseInt(tokenizer.nextToken()) - 1; // Ajuste Base 0
                int valor = Integer.parseInt(tokenizer.nextToken());

                persistance.update(posicion, valor, version);
            }
            else if (texto.equals("get")) {
                int version = Integer.parseInt(tokenizer.nextToken()) - 1;  // Ajuste Base 0
                int posicion = Integer.parseInt(tokenizer.nextToken()) - 1; // Ajuste Base 0

                // Consultamos la posición en la versión indicada
                int resultado = persistance.query(posicion, posicion, version);
                sb.append(resultado).append("\n");
            }
        }

        System.out.print(sb);
    }

    public static void problema3() throws IOException {

    }

    public static void main(String[] args) throws IOException {

    }
}

