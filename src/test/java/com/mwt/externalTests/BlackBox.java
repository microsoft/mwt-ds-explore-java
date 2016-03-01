package com.mwt.externalTests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mwt.explorers.*;
import com.mwt.policies.Policy;
import com.mwt.recorders.StringRecorder;
import com.mwt.utilities.MurMurHash3;
import com.mwt.utilities.PRG;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BlackBox {

    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.err.println("Wrong number of arguments");
            return;
        }

        try {
            final String content = IOUtils.toString(new File(args[0]).toURI(), Charset.forName("UTF-8"));

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
            System.out.println(ex);
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

        String appId = config.AppId;
        int numActions = config.NumberOfActions;
        String[] experimentalUnitIdList = config.ExperimentalUnitIdList;
        int tau = config.Tau;
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

                        TauFirstExplorer<EI.RegularTestContext> explorer =
                                new TauFirstExplorer<EI.RegularTestContext>(policy, tau, numActions);

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

                        VariableActionTauFirstExplorer<EI.VariableActionTestContext> explorer =
                                new VariableActionTauFirstExplorer<EI.VariableActionTestContext>(policy, tau);

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

    private static void testSoftmax(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);

        String appId = config.AppId;
        int numActions = config.NumberOfActions;
        String[] experimentalUnitIdList = config.ExperimentalUnitIdList;
        float lambda = config.Lambda;
        ScorerConfiguration configScorer = config.ScorerConfiguration;
        int scorerType = configScorer.ScorerType;

        switch (config.ContextType) {
            case 0: // fixed action context
            {
                StringRecorder<EI.RegularTestContext> recorder =
                        new StringRecorder<EI.RegularTestContext>();

                MwtExplorer<EI.RegularTestContext> mwt =
                        new MwtExplorer<EI.RegularTestContext>(appId, recorder);

                switch (scorerType) {
                    case 0: // fixed all-equal scorer
                    {
                        EI.TestScorer<EI.RegularTestContext> scorer =
                                new EI.TestScorer<EI.RegularTestContext>(configScorer.Score, numActions);

                        SoftmaxExplorer<EI.RegularTestContext> explorer =
                                new SoftmaxExplorer<EI.RegularTestContext>(scorer, lambda, numActions);

                        for (int i = 0; i < experimentalUnitIdList.length; i++) {
                            EI.RegularTestContext context = new EI.RegularTestContext();
                            context.Id = i;
                            mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                        }

                        pw.print(recorder.getRecording());
                        break;
                    }
                    case 1: // integer-progression scorer
                    {
                        EI.TestScorer<EI.RegularTestContext> scorer =
                                new EI.TestScorer<EI.RegularTestContext>(configScorer.Start, numActions, false);

                        SoftmaxExplorer<EI.RegularTestContext> explorer =
                                new SoftmaxExplorer<EI.RegularTestContext>(scorer, lambda, numActions);

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

                switch (scorerType) {
                    case 0: // fixed all-equal scorer
                    {
                        EI.TestScorer<EI.VariableActionTestContext> scorer =
                                new EI.TestScorer<EI.VariableActionTestContext>(configScorer.Score, numActions);

                        VariableActionSoftmaxExplorer<EI.VariableActionTestContext> explorer =
                                new VariableActionSoftmaxExplorer<EI.VariableActionTestContext>(scorer, lambda);

                        for (int i = 0; i < experimentalUnitIdList.length; i++) {
                            EI.VariableActionTestContext context = new EI.VariableActionTestContext(numActions);
                            context.Id = i;
                            mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                        }

                        pw.print(recorder.getRecording());
                        break;
                    }
                    case 1: // integer-progression scorer
                    {
                        EI.TestScorer<EI.VariableActionTestContext> scorer =
                                new EI.TestScorer<EI.VariableActionTestContext>(configScorer.Start, numActions, false);

                        VariableActionSoftmaxExplorer<EI.VariableActionTestContext> explorer =
                                new VariableActionSoftmaxExplorer<EI.VariableActionTestContext>(scorer, lambda);

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

    // TODO: refactor explore tests
    private static void testGeneric(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);

        String appId = config.AppId;
        int numActions = config.NumberOfActions;
        String[] experimentalUnitIdList = config.ExperimentalUnitIdList;
        ScorerConfiguration configScorer = config.ScorerConfiguration;
        int scorerType = configScorer.ScorerType;

        switch (config.ContextType) {
            case 0: // fixed action context
            {
                StringRecorder<EI.RegularTestContext> recorder =
                        new StringRecorder<EI.RegularTestContext>();

                MwtExplorer<EI.RegularTestContext> mwt =
                        new MwtExplorer<EI.RegularTestContext>(appId, recorder);

                switch (scorerType) {
                    case 0: // fixed all-equal scorer
                    {
                        EI.TestScorer<EI.RegularTestContext> scorer =
                                new EI.TestScorer<EI.RegularTestContext>(configScorer.Score, numActions);

                        GenericExplorer<EI.RegularTestContext> explorer =
                                new GenericExplorer<EI.RegularTestContext>(scorer, numActions);

                        for (int i = 0; i < experimentalUnitIdList.length; i++) {
                            EI.RegularTestContext context = new EI.RegularTestContext();
                            context.Id = i;
                            mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                        }

                        pw.print(recorder.getRecording());
                        break;
                    }
                    case 1: // integer-progression scorer
                    {
                        EI.TestScorer<EI.RegularTestContext> scorer =
                                new EI.TestScorer<EI.RegularTestContext>(configScorer.Start, numActions, false);

                        GenericExplorer<EI.RegularTestContext> explorer =
                                new GenericExplorer<EI.RegularTestContext>(scorer, numActions);

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

                switch (scorerType) {
                    case 0: // fixed all-equal scorer
                    {
                        EI.TestScorer<EI.VariableActionTestContext> scorer =
                                new EI.TestScorer<EI.VariableActionTestContext>(configScorer.Score, numActions);

                        VariableActionGenericExplorer<EI.VariableActionTestContext> explorer =
                                new VariableActionGenericExplorer<EI.VariableActionTestContext>(scorer);

                        for (int i = 0; i < experimentalUnitIdList.length; i++) {
                            EI.VariableActionTestContext context = new EI.VariableActionTestContext(numActions);
                            context.Id = i;
                            mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                        }

                        pw.print(recorder.getRecording());
                        break;
                    }
                    case 1: // integer-progression scorer
                    {
                        EI.TestScorer<EI.VariableActionTestContext> scorer =
                                new EI.TestScorer<EI.VariableActionTestContext>(configScorer.Start, numActions, false);

                        VariableActionGenericExplorer<EI.VariableActionTestContext> explorer =
                                new VariableActionGenericExplorer<EI.VariableActionTestContext>(scorer);

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

    private static void testBootstrap(TestConfiguration config) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(config.OutputFile);

        String appId = config.AppId;
        int numActions = config.NumberOfActions;
        String[] experimentalUnitIdList = config.ExperimentalUnitIdList;
        PolicyConfiguration[] configPolicies = config.PolicyConfigurations;

        switch (config.ContextType) {
            case 0: // fixed action context
            {
                StringRecorder<EI.RegularTestContext> recorder =
                        new StringRecorder<EI.RegularTestContext>();

                MwtExplorer<EI.RegularTestContext> mwt =
                        new MwtExplorer<EI.RegularTestContext>(appId, recorder);

                List<Policy<EI.RegularTestContext>> policies =
                        new ArrayList<Policy<EI.RegularTestContext>>();

                for (PolicyConfiguration configPolicy : configPolicies) {
                    switch (configPolicy.PolicyType) {
                        case 0: // fixed policy
                        {
                            EI.TestPolicy<EI.RegularTestContext> policy =
                                    new EI.TestPolicy<EI.RegularTestContext>();
                            policy.ActionToChoose = configPolicy.Action;
                            policies.add(policy);
                            break;
                        }
                    }
                }
                BootstrapExplorer<EI.RegularTestContext> explorer =
                        new BootstrapExplorer<EI.RegularTestContext>(policies, numActions);

                for (int i = 0; i < experimentalUnitIdList.length; i++) {
                    EI.RegularTestContext context = new EI.RegularTestContext();
                    context.Id = i;
                    mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                }

                pw.print(recorder.getRecording());
                break;
            }
            case 1: // variable action context
            {
                StringRecorder<EI.VariableActionTestContext> recorder =
                        new StringRecorder<EI.VariableActionTestContext>();

                MwtExplorer<EI.VariableActionTestContext> mwt =
                        new MwtExplorer<EI.VariableActionTestContext>(appId, recorder);

                List<Policy<EI.VariableActionTestContext>> policies =
                        new ArrayList<Policy<EI.VariableActionTestContext>>();

                for (PolicyConfiguration configPolicy : configPolicies) {
                    switch (configPolicy.PolicyType) {
                        case 0: // fixed policy
                        {
                            EI.TestPolicy<EI.VariableActionTestContext> policy =
                                    new EI.TestPolicy<EI.VariableActionTestContext>();
                            policy.ActionToChoose = configPolicy.Action;
                            policies.add(policy);
                            break;
                        }
                    }
                }
                VariableActionBootstrapExplorer<EI.VariableActionTestContext> explorer =
                        new VariableActionBootstrapExplorer<EI.VariableActionTestContext>(policies);

                for (int i = 0; i < experimentalUnitIdList.length; i++) {
                    EI.VariableActionTestContext context = new EI.VariableActionTestContext(numActions);
                    context.Id = i;
                    mwt.chooseAction(explorer, experimentalUnitIdList[i], context);
                }

                pw.print(recorder.getRecording());
                break;
            }
        }

        pw.close();
    }
}
