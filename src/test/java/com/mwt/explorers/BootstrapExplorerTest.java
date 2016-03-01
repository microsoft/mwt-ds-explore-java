package com.mwt.explorers;

import com.mwt.explorers.utilities.ExplorerInformation;
import com.mwt.explorers.utilities.FixedPolicy;
import com.mwt.explorers.utilities.FixedVariableActionContext;
import com.mwt.policies.Policy;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created by jmorra on 2/29/16.
 */
public class BootstrapExplorerTest {
    private final ExplorerInformation<String> stringExInfo = new ExplorerInformation<String>(8);
    private final ExplorerInformation<FixedVariableActionContext> varExInfo = new ExplorerInformation<FixedVariableActionContext>(8);
    private final List<Policy<String>> stringPolicies = new ArrayList<Policy<String>>();
    private final List<Policy<FixedVariableActionContext>> varPolicies = new ArrayList<Policy<FixedVariableActionContext>>();

    @Before
    public void setup() {
        for (int i=6; i<9; ++i) {
            stringPolicies.add(new FixedPolicy<String>(i));
            varPolicies.add(new FixedPolicy<FixedVariableActionContext>(i - 1));
        }
    }

    @Test
    public void fixedAction() {
        BootstrapExplorer<String> explorer = new BootstrapExplorer<String>(stringPolicies, 10);

        String context = "context";
        int action = stringExInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(8, action);
        assertEquals("8 abc 0.33333 | " + context, stringExInfo.recorder.getRecording().trim());
    }

    @Test
    public void variableAction() {
        BootstrapExplorer<FixedVariableActionContext> explorer = new BootstrapExplorer<FixedVariableActionContext>(varPolicies, 10);

        FixedVariableActionContext context = new FixedVariableActionContext(10);
        int action = varExInfo.mwt.chooseAction(explorer, "abc", context);

        assertEquals(7, action);
        assertEquals("7 abc 0.33333 | " + context, varExInfo.recorder.getRecording().trim());
    }
}
