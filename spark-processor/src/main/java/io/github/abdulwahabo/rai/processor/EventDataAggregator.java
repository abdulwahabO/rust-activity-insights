package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import io.github.abdulwahabo.rai.processor.model.EventData;
import java.util.List;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.expressions.Aggregator;

public class EventDataAggregator extends
        Aggregator<EventData, List<AggregateEventData.RepositoryData>, List<AggregateEventData.RepositoryData>> {

    @Override
    public List<AggregateEventData.RepositoryData> zero() {
        return null;
    }

    @Override
    public List<AggregateEventData.RepositoryData> reduce(
            List<AggregateEventData.RepositoryData> b, EventData a) {
        return null;
    }

    @Override
    public List<AggregateEventData.RepositoryData> merge(
            List<AggregateEventData.RepositoryData> b1, List<AggregateEventData.RepositoryData> b2) {
        return null;
    }

    @Override
    public List<AggregateEventData.RepositoryData> finish(List<AggregateEventData.RepositoryData> reduction) {
        return null;
    }

    @Override
    public Encoder<List<AggregateEventData.RepositoryData>> bufferEncoder() {
        return null;
    }

    @Override
    public Encoder<List<AggregateEventData.RepositoryData>> outputEncoder() {
        return null;
    }
}
