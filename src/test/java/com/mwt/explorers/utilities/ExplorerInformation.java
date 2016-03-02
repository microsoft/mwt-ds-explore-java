package com.mwt.explorers.utilities;

import com.mwt.explorers.MwtExplorer;
import com.mwt.explorers.utilities.FixedPolicy;
import com.mwt.recorders.StringRecorder;

/**
 * Created by jmorra on 2/29/16.
 */
public class ExplorerInformation<T> {

    public final FixedPolicy<T> policy;
    public final StringRecorder<T> recorder;
    public final MwtExplorer<T> mwt;

    public ExplorerInformation(final int action) {
        policy = new FixedPolicy<T>(action);
        recorder = new StringRecorder<T>();
        mwt = new MwtExplorer<T>("1", recorder);
    }
}
