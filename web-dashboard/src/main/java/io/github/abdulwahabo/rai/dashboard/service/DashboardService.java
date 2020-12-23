package io.github.abdulwahabo.rai.dashboard.service;

import io.github.abdulwahabo.rai.dashboard.dao.AggregateEventDataDao;
import io.github.abdulwahabo.rai.dashboard.model.AggregateEventData;
import io.github.abdulwahabo.rai.dashboard.model.DashboardDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: use orchestration tool for this project.
//      Benefits: if Spark job errors for example, another extractor job won't overwrite unprocessed raw data.

@Service
public class DashboardService {

    private AggregateEventDataDao dataDao;

    @Autowired
    public DashboardService(AggregateEventDataDao dataDao) {
        this.dataDao = dataDao;
    }

    public DashboardDataDto getData(String startDate, String endDate) {

        Optional<List<AggregateEventData>> optionalList = dataDao.get(startDate, endDate);
        if (optionalList.isPresent()) {
            Map<String, AggregateEventData.RepositoryData> repositoryDataMap = new HashMap<>();
            List<AggregateEventData> aggregateEventData = optionalList.get();

            for (AggregateEventData eventData : aggregateEventData) {
                List<AggregateEventData.RepositoryData> repositoryData = eventData.getRepositoryData();
                for (AggregateEventData.RepositoryData data : repositoryData) {
                    repositoryDataMap.putIfAbsent(data.getRepository(), data);
                    repositoryDataMap.computeIfPresent(data.getRepository(), (key, value) -> {
                        value.setBranches(value.getBranches() + data.getBranches());
                        value.setForks(value.getForks() + data.getForks());
                        value.setPushes(value.getPushes() + data.getPushes());
                        value.setWatcher(value.getWatcher() + data.getWatcher());
                        value.setPullRequestReviewComments(
                                value.getPullRequestReviewComments() + data.getPullRequestReviewComments());
                        return value;
                    });
                }
            }
            List<AggregateEventData.RepositoryData> repositoryData = new ArrayList<>(repositoryDataMap.values());
            DashboardDataDto dashboardDataDto = new DashboardDataDto();
            dashboardDataDto.setRepositoryActivityTotal(repositoryData);
            Map<String, Integer> topPushes = topPushes(repositoryData);
            dashboardDataDto.setPushesPerRepo(topPushes);
            Map<String, Double> percentPerActivity = percentPerActivity(repositoryData);
            dashboardDataDto.setActivitiesPercentage(percentPerActivity);

            return dashboardDataDto;
        } else {
            return new DashboardDataDto();
        }
    }

    // TODO: Explain what this does....
    private Map<String, Integer> topPushes(List<AggregateEventData.RepositoryData> repositoryData) {

        repositoryData.sort(Comparator.comparingInt(AggregateEventData.RepositoryData::getPushes));
        return repositoryData.stream()
                             .limit(5)
                             .collect(Collectors.toMap(AggregateEventData.RepositoryData::getRepository,
                                     AggregateEventData.RepositoryData::getPushes));
    }

    // TODO: explain what this does..,..
    private Map<String, Double> percentPerActivity(List<AggregateEventData.RepositoryData> repositoryData) {

        int totalPushes = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getPushes).sum();
        int totalForks = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getForks).sum();
        int totalBranches = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getBranches).sum();
        int totalWatchers = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getWatcher).sum();
        int totalPRreviewComments = repositoryData.stream()
                                                  .mapToInt(
                                                          AggregateEventData.RepositoryData::getPullRequestReviewComments)
                                                  .sum();

        int totalNewActivity = totalBranches + totalForks + totalPRreviewComments + totalWatchers + totalPushes;

        Map<String, Double> percentPerActivity = new HashMap<>();
        percentPerActivity.put("pushes", percentageOfTotal(totalNewActivity, totalPushes));
        percentPerActivity.put("forks", percentageOfTotal(totalNewActivity, totalForks));
        percentPerActivity.put("branches", percentageOfTotal(totalNewActivity, totalBranches));
        percentPerActivity.put("pr_comments", percentageOfTotal(totalNewActivity, totalPRreviewComments));
        percentPerActivity.put("watchers", percentageOfTotal(totalNewActivity, totalWatchers));
        return percentPerActivity;
    }



    // Calculates the percentage of 'value' in 'total'
    private double percentageOfTotal(double total, double value) {
        if (total == 0.0 || value == 0.0) {
            return 0.0;
        }
        double percentage = (value / total) * 100;
        BigDecimal bd = new BigDecimal(percentage);
        return bd.setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
