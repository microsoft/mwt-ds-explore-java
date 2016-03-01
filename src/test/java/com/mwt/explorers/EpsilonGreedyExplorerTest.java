package com.mwt.explorers;

import com.mwt.explorers.utilities.ExplorerInformation;
import com.mwt.explorers.utilities.FixedVariableActionContext;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jmorra on 2/29/16.
 */
public class EpsilonGreedyExplorerTest {

    private final ExplorerInformation<String> exInfo = new ExplorerInformation<String>(8);

    @Test
    public void fixedPolicyLowEpsilon() {
        EpsilonGreedyExplorer<String> explorer = new EpsilonGreedyExplorer<String>(exInfo.policy, 0.1f, 10);

        String context = "context";
        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, action);
        assertEquals("8 abc 0.91000 | " + context, exInfo.recorder.getRecording().trim());
    }

    @Test
    public void fixedPolicyHighEpsilon() {
        EpsilonGreedyExplorer<String> explorer = new EpsilonGreedyExplorer<String>(exInfo.policy, 0.9f, 10);

        String context = "context";
        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, action);
        assertEquals("7 abc 0.09000 | " + context, exInfo.recorder.getRecording().trim());
    }

    @Test
    public void variableActionContext() {
        ExplorerInformation<FixedVariableActionContext> exInfo = new ExplorerInformation<FixedVariableActionContext>(8);

        VariableActionEpsilonGreedyExplorer<FixedVariableActionContext> explorer = new VariableActionEpsilonGreedyExplorer<FixedVariableActionContext>(exInfo.policy, 0.1f);

        FixedVariableActionContext context = new FixedVariableActionContext(10);
        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, action);
        assertEquals("8 abc 0.91000 | " + context, exInfo.recorder.getRecording().trim());
    }
}
