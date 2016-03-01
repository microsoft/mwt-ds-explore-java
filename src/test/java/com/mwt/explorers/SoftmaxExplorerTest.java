package com.mwt.explorers;

import com.mwt.explorers.utilities.ExplorerInformation;
import com.mwt.explorers.utilities.FixedScorer;
import com.mwt.explorers.utilities.FixedVariableActionContext;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jmorra on 2/29/16.
 */
public class SoftmaxExplorerTest {
    private final ExplorerInformation<String> stringExInfo = new ExplorerInformation<String>(8);
    private final ExplorerInformation<FixedVariableActionContext> varExInfo = new ExplorerInformation<FixedVariableActionContext>(8);

    @Test
    public void fixedAllEqualScorer() {
        FixedScorer<String> scorer = new FixedScorer<String>(1, 1);
        SoftmaxExplorer<String> explorer = new SoftmaxExplorer<String>(scorer, 0.1f, 2);

        String context = "context";
        int action = stringExInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(2, action);
        assertEquals("2 abc 0.50000 | " + context, stringExInfo.recorder.getRecording().trim());
    }

    @Test
    public void fixedIntegerProgressionScorer() {
        FixedScorer<String> scorer = new FixedScorer<String>(1, 2, 3);
        SoftmaxExplorer<String> explorer = new SoftmaxExplorer<String>(scorer, 0.1f, 3);

        String context = "context";
        int action = stringExInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(3, action);
        assertEquals("3 abc 0.36717 | " + context, stringExInfo.recorder.getRecording().trim());
    }

    @Test
    public void variableAllEqualScorer() {
        FixedScorer<FixedVariableActionContext> scorer = new FixedScorer<FixedVariableActionContext>(1, 1);
        SoftmaxExplorer<FixedVariableActionContext> explorer = new SoftmaxExplorer<FixedVariableActionContext>(scorer, 0.1f, 2);

        FixedVariableActionContext context = new FixedVariableActionContext(2);
        int action = varExInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(2, action);
        assertEquals("2 abc 0.50000 | " + context, varExInfo.recorder.getRecording().trim());
    }

    @Test
    public void variableIntegerProgressionScorer() {
        FixedScorer<FixedVariableActionContext> scorer = new FixedScorer<FixedVariableActionContext>(1, 2, 3);
        SoftmaxExplorer<FixedVariableActionContext> explorer = new SoftmaxExplorer<FixedVariableActionContext>(scorer, 0.1f, 3);

        FixedVariableActionContext context = new FixedVariableActionContext(3);
        int action = varExInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(3, action);
        assertEquals("3 abc 0.36717 | " + context, varExInfo.recorder.getRecording().trim());
    }
}
