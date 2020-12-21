package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import io.github.abdulwahabo.rai.processor.model.EventData;

import java.util.List;
import java.util.Optional;
import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.expressions.Aggregator;

public class EventDataAggregator extends
        Aggregator<EventData, AggregateEventData.RepositoryDataWrapper, AggregateEventData.RepositoryDataWrapper> {

    @Override
    public AggregateEventData.RepositoryDataWrapper zero() {
        return new AggregateEventData.RepositoryDataWrapper();
    }

    @Override
    public AggregateEventData.RepositoryDataWrapper reduce(
            AggregateEventData.RepositoryDataWrapper buffer,
            EventData eventData) {

        List<AggregateEventData.RepositoryData> repositoryDataList = buffer.getRepositoryDataList();

        Optional<AggregateEventData.RepositoryData> dataOptional =
                repositoryDataList.stream()
                                  .filter(repositoryData -> repositoryData.getRepository()
                                                                          .equals(eventData.getRepository()))
                                  .findAny();

        AggregateEventData.RepositoryData repositoryData =
                dataOptional.orElseGet(() -> {
                    AggregateEventData.RepositoryData repositoryData1 = new AggregateEventData.RepositoryData();
                    repositoryData1.setRepository(eventData.getRepository());
                    return repositoryData1;
                });

        switch (eventData.getType()) {
            case "CreateEvent":
                repositoryData.setBranches(repositoryData.getBranches() + 1);
                break;
            case "WatchEvent":
                repositoryData.setWatcher(repositoryData.getWatcher() + 1);
                break;
            case "PushEvent":
                repositoryData.setPushes(repositoryData.getPushes() + 1);
                break;
            case "PullRequestReviewCommentEvent":
                repositoryData.setPullRequestReviewComments(repositoryData.getPullRequestReviewComments() + 1);
                break;
            case "ForkEvent":
                repositoryData.setForks(repositoryData.getForks() + 1);
                break;
        }

        buffer.setRepositoryDataList(repositoryDataList);
        return buffer;
    }

    @Override
    public AggregateEventData.RepositoryDataWrapper merge(
            AggregateEventData.RepositoryDataWrapper b1,
            AggregateEventData.RepositoryDataWrapper b2) {

        List<AggregateEventData.RepositoryData> repositoryDataList1 = b1.getRepositoryDataList();
        List<AggregateEventData.RepositoryData> repositoryDataList2 = b2.getRepositoryDataList();

        for (AggregateEventData.RepositoryData repositoryData1 : repositoryDataList1) {
            for (AggregateEventData.RepositoryData repositoryData2 : repositoryDataList2) {
                if (repositoryData1.getRepository().equalsIgnoreCase(repositoryData2.getRepository())) {

                    repositoryData1.setForks(repositoryData1.getForks() + repositoryData2.getForks());
                    repositoryData1.setPushes(repositoryData1.getPushes() + repositoryData2.getPushes());
                    repositoryData1.setBranches(repositoryData1.getBranches() + repositoryData2.getBranches());
                    repositoryData1.setWatcher(repositoryData1.getWatcher() + repositoryData2.getWatcher());
                    repositoryData1.setPullRequestReviewComments(repositoryData1.getPullRequestReviewComments()
                            + repositoryData2.getPullRequestReviewComments());
                    break;
                }
            }
        }

        b1.setRepositoryDataList(repositoryDataList1);
        return b1;
    }

    @Override
    public AggregateEventData.RepositoryDataWrapper finish(AggregateEventData.RepositoryDataWrapper reduction) {
        return reduction;
    }

    @Override
    public Encoder<AggregateEventData.RepositoryDataWrapper> bufferEncoder() {
        return Encoders.bean(AggregateEventData.RepositoryDataWrapper.class);
    }

    @Override
    public Encoder<AggregateEventData.RepositoryDataWrapper> outputEncoder() {
        return Encoders.bean(AggregateEventData.RepositoryDataWrapper.class);
    }
}
