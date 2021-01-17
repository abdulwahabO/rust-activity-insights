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
                List<AggregateEventData.RepositoryData> repositoryDataList = eventData.getRepositoryData();
                for (AggregateEventData.RepositoryData data : repositoryDataList) {
                    if (repositoryDataMap.containsKey(data.getRepository())) {
                        AggregateEventData.RepositoryData oldValue = repositoryDataMap.get(data.getRepository());
                        oldValue.setBranches(oldValue.getBranches() + data.getBranches());
                        oldValue.setIssuesEvents(oldValue.getIssuesEvents() + data.getIssuesEvents());
                        oldValue.setPushes(oldValue.getPushes() + data.getPushes());
                        oldValue.setWatcher(oldValue.getWatcher() + data.getWatcher());
                        oldValue.setPullRequestEvents(oldValue.getPullRequestEvents() + data.getPullRequestEvents());
                        repositoryDataMap.put(data.getRepository(), oldValue);
                    } else {
                        repositoryDataMap.put(data.getRepository(), data);
                    }
                }
            }
            List<AggregateEventData.RepositoryData> repositoryData = new ArrayList<>(repositoryDataMap.values());
            DashboardDataDto dashboardDataDto = new DashboardDataDto();

            Map<String, Integer> topIssuesActivity = topIssuesActivity(repositoryData);
            dashboardDataDto.setTopIssuesActivity(topIssuesActivity);
            Map<String, Double> percentPerActivity = percentPerActivity(repositoryData);
            dashboardDataDto.setActivitiesPercentage(percentPerActivity);
            return dashboardDataDto;
        } else {
            return new DashboardDataDto();
        }
    }

    // Returns the top five repositories in terms of issues activity.
    private Map<String, Integer> topIssuesActivity(List<AggregateEventData.RepositoryData> repositoryData) {
        repositoryData.sort(Comparator.comparingInt(AggregateEventData.RepositoryData::getIssuesEvents).reversed());
        return repositoryData.stream()
                             .limit(5)
                             .collect(Collectors.toMap(AggregateEventData.RepositoryData::getRepository,
                                     AggregateEventData.RepositoryData::getIssuesEvents));
    }

    // Calculate the percentage of events contributed by each activity
    private Map<String, Double> percentPerActivity(List<AggregateEventData.RepositoryData> repositoryData) {

        int totalPushes = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getPushes).sum();
        int totalIssuesActivity = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getIssuesEvents).sum();
        int totalBranches = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getBranches).sum();
        int totalWatchers = repositoryData.stream().mapToInt(AggregateEventData.RepositoryData::getWatcher).sum();
        int totalPrActivity = repositoryData.stream()
                                                  .mapToInt(
                                                          AggregateEventData.RepositoryData::getPullRequestEvents)
                                                  .sum();

        int totalNewActivity = totalBranches + totalIssuesActivity + totalPrActivity + totalWatchers + totalPushes;

        Map<String, Double> percentPerActivity = new HashMap<>();
        percentPerActivity.put("pushes", percentageOfTotal(totalNewActivity, totalPushes));
        percentPerActivity.put("issues_activity", percentageOfTotal(totalNewActivity, totalIssuesActivity));
        percentPerActivity.put("branches", percentageOfTotal(totalNewActivity, totalBranches));
        percentPerActivity.put("pull_request_activity", percentageOfTotal(totalNewActivity, totalPrActivity));
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
