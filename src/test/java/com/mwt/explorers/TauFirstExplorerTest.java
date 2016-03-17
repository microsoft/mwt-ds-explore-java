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
    private final ExplorerInformation<FixedVariableActionContext> varExInfo = new ExplorerInformation<FixedVariableActionContext>(8);

    @Test
    public void fixedPolicyLessThanTau() {
        lessThanTau("context", exInfo);
    }

    @Test
    public void fixedPolicyGreaterThanTau() {
        greaterThanTau("context", exInfo);
    }

    @Test
    public void variableActionPolicyLessThanTau() {
        lessThanTau(new FixedVariableActionContext(10), varExInfo);
    }

    @Test
    public void variableActionPolicyGreaterThanTau() {
        greaterThanTau(new FixedVariableActionContext(10), varExInfo);
    }

    private <T> void lessThanTau(T context, ExplorerInformation<T> exInfo) {
        TauFirstExplorer<T> explorer = new TauFirstExplorer<T>(exInfo.policy, 3, 10);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, action);
        assertEquals("7 abc 0.10000 | " + context, exInfo.recorder.getRecording().trim());

        int secondAction = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, secondAction);
        assertEquals("7 abc 0.10000 | " + context, exInfo.recorder.getRecording().trim());
    }

    private <T> void greaterThanTau(T context, ExplorerInformation<T> exInfo) {
        TauFirstExplorer<T> explorer = new TauFirstExplorer<T>(exInfo.policy, 0, 10);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, action);
        assertEquals("8 abc 1.00000 | " + context, exInfo.recorder.getRecording().trim());

        int secondAction = exInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, secondAction);
        assertEquals("8 abc 1.00000 | " + context, exInfo.recorder.getRecording().trim());
    }
}
