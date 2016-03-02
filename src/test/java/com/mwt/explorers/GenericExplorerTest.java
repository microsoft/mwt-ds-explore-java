package com.mwt.explorers;

import com.mwt.explorers.utilities.ExplorerInformation;
import com.mwt.explorers.utilities.FixedScorer;
import com.mwt.explorers.utilities.FixedVariableActionContext;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jmorra on 2/29/16.
 */
public class GenericExplorerTest {

    private final ExplorerInformation<String> exInfo = new ExplorerInformation<String>(8);
    private final ExplorerInformation<FixedVariableActionContext> varExInfo = new ExplorerInformation<FixedVariableActionContext>(8);

    @Test
    public void fixedAllEqualScorer() {
        allEqualScorer("context", exInfo);
    }

    @Test
    public void fixedIntegerProgressionScorer() {
        integerProgressionScorer("context", exInfo);
    }

    @Test
    public void variableAllEqualScorer() {
        allEqualScorer(new FixedVariableActionContext(3), varExInfo);
    }

    @Test
    public void variableIntegerProgressionScorer() {
        integerProgressionScorer(new FixedVariableActionContext(3), varExInfo);
    }

    private <T> void allEqualScorer(T context, ExplorerInformation<T> exInfo) {
        FixedScorer<T> scorer = new FixedScorer<T>(1, 1);
        GenericExplorer<T> explorer = new GenericExplorer<T>(scorer, 2);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(2, action);
        assertEquals("2 abc 0.50000 | " + context, exInfo.recorder.getRecording().trim());
    }

    private <T> void integerProgressionScorer(T context, ExplorerInformation<T> exInfo) {
        FixedScorer<T> scorer = new FixedScorer<T>(1, 2, 3);
        GenericExplorer<T> explorer = new GenericExplorer<T>(scorer, 3);

        int action = exInfo.mwt.chooseAction(explorer, "abc", context);
        assertEquals(3, action);
        assertEquals("3 abc 0.50000 | " + context, exInfo.recorder.getRecording().trim());
    }
}
