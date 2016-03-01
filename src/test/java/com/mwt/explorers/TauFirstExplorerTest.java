package com.mwt.explorers;

import com.mwt.explorers.utilities.ExplorerInformation;
import com.mwt.explorers.utilities.FixedVariableActionContext;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jmorra on 2/29/16.
 */
public class TauFirstExplorerTest {
    private final ExplorerInformation<String> exInfo = new ExplorerInformation<String>(8);

    @Test
    public void fixedPolicyLessThanTau() {
        TauFirstExplorer<String> explorer = new TauFirstExplorer<String>(exInfo.policy, 3, 10);

        String context = "context";
        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, action);
        assertEquals("7 abc 0.10000 | " + context, exInfo.recorder.getRecording().trim());

        int secondAction = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, secondAction);
        assertEquals("7 abc 0.10000 | " + context, exInfo.recorder.getRecording().trim());
    }

    @Test
    public void fixedPolicyGreaterThanTau() {
        TauFirstExplorer<String> explorer = new TauFirstExplorer<String>(exInfo.policy, 0, 10);

        String context = "context";
        exInfo.mwt.chooseAction(explorer, "abc", context);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, action);
        assertTrue(exInfo.recorder.getRecording().trim().isEmpty());

        int secondAction = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, secondAction);
        assertTrue(exInfo.recorder.getRecording().trim().isEmpty());
    }

    @Test
    public void variableActionPolicyLessThanTau() {
        ExplorerInformation<FixedVariableActionContext> exInfo = new ExplorerInformation<FixedVariableActionContext>(8);

        TauFirstExplorer<FixedVariableActionContext> explorer = new TauFirstExplorer<FixedVariableActionContext>(exInfo.policy, 3, 10);

        FixedVariableActionContext context = new FixedVariableActionContext(10);
        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, action);
        assertEquals("7 abc 0.10000 | " + context, exInfo.recorder.getRecording().trim());
    }
}
