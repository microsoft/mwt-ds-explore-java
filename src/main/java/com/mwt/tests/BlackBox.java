package com.mwt.tests;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mwt.utilities.MurMurHash3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BlackBox {

    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            return;
        }

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(args[0]));
            String content = new String(encoded, StandardCharsets.UTF_8);

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();

            TestConfiguration[] configs = gson.fromJson(content, TestConfiguration[].class);
            for (TestConfiguration config : configs) {
                switch (config.Type) {
                    case 0:
                        testPrg(config);
                        break;
                    case 1:
                        testHash(config);
                        break;
                    case 2:
                        testEpsilonGreedy(config);
                        break;
                    case 3:
                        testTauFirst(config);
                        break;
                    case 4:
                        testSoftmax(config);
                        break;
                    case 5:
                        testGeneric(config);
                        break;
                    case 6:
                        testBootstrap(config);
                        break;
                }
            }
        }
        catch (IOException ex) {
            // Do nothing
        }
    }

    private static void testPrg(TestConfiguration config) {
    }

    private static void testHash(TestConfiguration config) throws FileNotFoundException {
        String[] values = config.Values;
        PrintWriter pw = new PrintWriter(config.OutputFile);
        for (String v : values) {
            pw.println(((Long) MurMurHash3.computeIdHash(v)).toString());
        }
        pw.close();
    }

    private static void testEpsilonGreedy(TestConfiguration config) {
    }

    private static void testTauFirst(TestConfiguration config) {
    }

    private static void testSoftmax(TestConfiguration config) {
    }

    private static void testGeneric(TestConfiguration config) {
    }

    private static void testBootstrap(TestConfiguration config) {
    }
}
