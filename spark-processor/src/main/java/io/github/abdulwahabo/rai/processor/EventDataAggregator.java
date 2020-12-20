package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import io.github.abdulwahabo.rai.processor.model.EventData;
import java.util.ArrayList;
import java.util.List;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.expressions.Aggregator;

public class EventDataAggregator extends
        Aggregator<EventData, List<AggregateEventData.RepositoryData>, List<AggregateEventData.RepositoryData>> {

    @Override
    public List<AggregateEventData.RepositoryData> zero() {
        return new ArrayList<>();
    }

    @Override
    public List<AggregateEventData.RepositoryData> reduce(
            List<AggregateEventData.RepositoryData> repositoryData, EventData eventData) {
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

    // TODO: Problem:: Can't create Encoder for List type.
    //      Solution: Create a wrapper type around List<Agg.RepoData> and use that type here.

    @Override
    public Encoder<List<AggregateEventData.RepositoryData>> bufferEncoder() {
        Encoder<List<AggregateEventData.RepositoryData>> encoder =
                Encoders.bean(List<AggregateEventData.RepositoryData>.class);
    }

    @Override
    public Encoder<List<AggregateEventData.RepositoryData>> outputEncoder() {
        return null;
    }
}
