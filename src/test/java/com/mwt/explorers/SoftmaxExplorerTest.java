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
        allEqualScorer("context", stringExInfo);
    }

    @Test
    public void fixedIntegerProgressionScorer() {
        integerProgressionScorer("context", stringExInfo);
    }

    @Test
    public void variableAllEqualScorer() {
        allEqualScorer(new FixedVariableActionContext(2), varExInfo);
    }

    @Test
    public void variableIntegerProgressionScorer() {
        integerProgressionScorer(new FixedVariableActionContext(3), varExInfo);
    }

    private <T> void allEqualScorer(T context, ExplorerInformation<T> exInfo) {
        FixedScorer<T> scorer = new FixedScorer<T>(1, 1);
        SoftmaxExplorer<T> explorer = new SoftmaxExplorer<T>(scorer, 0.1f, 2);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(2, action);
        assertEquals("2 abc 0.50000 | " + context, exInfo.recorder.getRecording().trim());
    }

    private <T> void integerProgressionScorer(T context, ExplorerInformation<T> exInfo) {
        FixedScorer<T> scorer = new FixedScorer<T>(1, 2, 3);
        SoftmaxExplorer<T> explorer = new SoftmaxExplorer<T>(scorer, 0.1f, 3);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(3, action);
        assertEquals("3 abc 0.36717 | " + context, exInfo.recorder.getRecording().trim());
    }
}
