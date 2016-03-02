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
    private final ExplorerInformation<FixedVariableActionContext> varExInfo = new ExplorerInformation<FixedVariableActionContext>(8);

    @Test
    public void fixedPolicyLowEpsilon() {
        lowEpsilon("context", exInfo);
    }

    @Test
    public void fixedPolicyHighEpsilon() {
        highEpsilon("context", exInfo);
    }

    @Test
    public void variableActionLowEpsilon() {
        lowEpsilon(new FixedVariableActionContext(10), varExInfo);
    }

    @Test
    public void variableActionHighEpsilon() {
        highEpsilon(new FixedVariableActionContext(10), varExInfo);
    }

    private <T> void lowEpsilon(T context, ExplorerInformation<T> exInfo) {
        EpsilonGreedyExplorer<T> explorer = new EpsilonGreedyExplorer<T>(exInfo.policy, 0.1f, 10);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, action);
        assertEquals("8 abc 0.91000 | " + context, exInfo.recorder.getRecording().trim());
    }

    private <T> void highEpsilon(T context, ExplorerInformation<T> exInfo) {
        EpsilonGreedyExplorer<T> explorer = new EpsilonGreedyExplorer<T>(exInfo.policy, 0.9f, 10);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, action);
        assertEquals("7 abc 0.09000 | " + context, exInfo.recorder.getRecording().trim());
    }
}
