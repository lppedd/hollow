package com.netflix.vms.transformer.publish.workflow;

import com.netflix.hollow.api.producer.HollowProducer;
import com.netflix.hollow.core.read.engine.HollowReadStateEngine;
import com.netflix.vms.transformer.publish.status.CycleStatusFuture;
import java.util.function.LongSupplier;

public interface PublishWorkflowStager {
    
    CycleStatusFuture triggerPublish(long inputDataVersion, long previousCycleId, long currentCycleId);
    
    void notifyRestoredStateEngine(HollowReadStateEngine stateEngine, HollowReadStateEngine nostreamsRestoredState);
    
    HollowReadStateEngine getCurrentReadStateEngine();

    PublishWorkflowContext getContext();

    default void initProducer(LongSupplier inputVersion, HollowProducer p, String vip) {
    }

    default void initNoStreamsProducer(LongSupplier inputVersion, HollowProducer p, String vip) {
    }
}
