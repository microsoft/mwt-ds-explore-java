package com.mwt.tests;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mwt.contexts.*;
import com.mwt.explorers.*;
import com.mwt.recorders.*;
import com.mwt.utilities.*;

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

    private static void testPrg(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);
        PRG prg = new PRG(config.Seed);

        for (int i = 0; i < config.Iterations; i++) {
            if (config.UniformInterval == null) {
                pw.println(prg.uniformUnitInterval());
            }
            else {
                pw.println(prg.uniformInt(config.UniformInterval.Item1, config.UniformInterval.Item2));
            }
        }

        pw.close();
    }

    private static void testHash(TestConfiguration config) throws FileNotFoundException {
        String[] values = config.Values;
        PrintWriter pw = new PrintWriter(config.OutputFile);
        for (String v : values) {
            pw.println(((Long) MurMurHash3.computeIdHash(v)).toString());
        }
        pw.close();
    }

    private static void testEpsilonGreedy(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);

        String appId = config.AppId;
        int numActions = config.NumberOfActions;
        String[] experimentalUnitIdList = config.ExperimentalUnitIdList;
        float epsilon = config.Epsilon;
        PolicyConfiguration configPolicy = config.PolicyConfiguration;
        int policyType = configPolicy.PolicyType;

        switch (config.ContextType) {
            case 0: // fixed action context
            {
                StringRecorder<EI.RegularTestContext> recorder =
                        new StringRecorder<EI.RegularTestContext>();

                MwtExplorer<EI.RegularTestContext> mwt =
                        new MwtExplorer<EI.RegularTestContext>(appId, recorder);

                switch (policyType) {
                    case 0: // fixed policy
                    {
                        EI.TestPolicy<EI.RegularTestContext> policy =
                                new EI.TestPolicy<EI.RegularTestContext>();

                        policy.ActionToChoose = configPolicy.Action;

                        EpsilonGreedyExplorer<EI.RegularTestContext> explorer =
                                new EpsilonGreedyExplorer<EI.RegularTestContext>(policy, epsilon, numActions);

                        for (int i = 0; i < experimentalUnitIdList.length; i++) {
                            EI.RegularTestContext context = new EI.RegularTestContext();
                            context.Id = i;
                            mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                        }

                        pw.print(recorder.getRecording());
                        break;
                    }
                }
                break;
            }
            case 1: // variable action context
            {
                StringRecorder<EI.VariableActionTestContext> recorder =
                        new StringRecorder<EI.VariableActionTestContext>();

                MwtExplorer<EI.VariableActionTestContext> mwt =
                        new MwtExplorer<EI.VariableActionTestContext>(appId, recorder);

                switch (policyType) {
                    case 0: // fixed policy
                    {
                        EI.TestPolicy<EI.VariableActionTestContext> policy =
                                new EI.TestPolicy<EI.VariableActionTestContext>();

                        policy.ActionToChoose = configPolicy.Action;

                        VariableActionEpsilonGreedyExplorer<EI.VariableActionTestContext> explorer =
                                new VariableActionEpsilonGreedyExplorer<EI.VariableActionTestContext>(policy, epsilon);

                        for (int i = 0; i < experimentalUnitIdList.length; i++) {
                            EI.VariableActionTestContext context = new EI.VariableActionTestContext(numActions);
                            context.Id = i;
                            mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                        }

                        pw.print(recorder.getRecording());
                        break;
                    }
                }
                break;
            }
        }

        pw.close();
    }

    private static void testTauFirst(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);
        pw.close();
    }

    private static void testSoftmax(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);
        pw.close();
    }

    private static void testGeneric(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);
        pw.close();
    }

    private static void testBootstrap(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);
        pw.close();
    }
}
