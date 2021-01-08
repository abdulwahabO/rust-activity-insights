package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import io.github.abdulwahabo.rai.processor.model.EventData;

import java.util.ArrayList;
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
        List<AggregateEventData.RepositoryData> oldBufferList = buffer.getRepositoryDataList();
        Optional<AggregateEventData.RepositoryData> dataOptional =
                oldBufferList.stream()
                                  .filter(repositoryData -> repositoryData.getRepository()
                                                                          .equalsIgnoreCase(eventData.getRepository()))
                                  .findFirst();

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
            case "PullRequestEvent":
                repositoryData.setPullRequestEvents(repositoryData.getPullRequestEvents() + 1);
                break;
            case "IssueCommentEvent":
            case "IssuesEvent":
                repositoryData.setIssuesEvents(repositoryData.getIssuesEvents() + 1);
                break;
        }
        oldBufferList.removeIf(oldEntry -> oldEntry.getRepository().equalsIgnoreCase(repositoryData.getRepository()));
        oldBufferList.add(repositoryData);
        buffer.setRepositoryDataList(oldBufferList);
        return buffer;
    }

    @Override
    public AggregateEventData.RepositoryDataWrapper merge(
            AggregateEventData.RepositoryDataWrapper b1,
            AggregateEventData.RepositoryDataWrapper b2) {

        List<AggregateEventData.RepositoryData> repositoryDataList1 = b1.getRepositoryDataList();
        List<AggregateEventData.RepositoryData> repositoryDataList2 = b2.getRepositoryDataList();
        List<AggregateEventData.RepositoryData> resultList = new ArrayList<>();
        AggregateEventData.RepositoryDataWrapper result = new AggregateEventData.RepositoryDataWrapper();

        if (repositoryDataList1.size() > 0 && repositoryDataList2.size() <= 0) {
          return b1;
        } else if (repositoryDataList2.size() > 0 && repositoryDataList1.size() <= 0) {
            return b2;
        } else {
            for (AggregateEventData.RepositoryData repositoryData1 : repositoryDataList1) {
                for (AggregateEventData.RepositoryData repositoryData2 : repositoryDataList2) {
                    if (repositoryData1.getRepository().equalsIgnoreCase(repositoryData2.getRepository())) {
                        AggregateEventData.RepositoryData newData = new AggregateEventData.RepositoryData();
                        newData.setIssuesEvents(repositoryData1.getIssuesEvents() + repositoryData2.getIssuesEvents());
                        newData.setPushes(repositoryData1.getPushes() + repositoryData2.getPushes());
                        newData.setBranches(repositoryData1.getBranches() + repositoryData2.getBranches());
                        newData.setWatcher(repositoryData1.getWatcher() + repositoryData2.getWatcher());
                        newData.setPullRequestEvents(repositoryData1.getPullRequestEvents()
                                + repositoryData2.getPullRequestEvents());
                        resultList.add(newData);
                        break;
                    }
                }
            }
        }
        result.setRepositoryDataList(resultList);
        return result;
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
