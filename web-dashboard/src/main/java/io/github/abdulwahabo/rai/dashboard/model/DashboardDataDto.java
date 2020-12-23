package io.github.abdulwahabo.rai.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardDataDto {

    // todo; use for simple barchart.
    @JsonProperty("repos_pushes")
    private Map<String, Integer> pushesPerRepo;

    // todo: use for for doughnut pie chart.
    @JsonProperty("activities_percentage")
    private Map<String, Double> activitiesPercentage;
    // todo: percentages of each activity for period.

    // todo: Table.
    @JsonProperty("repos_activities_total")
    private List<AggregateEventData.RepositoryData> repositoryActivityTotal;

    public List<AggregateEventData.RepositoryData> getRepositoryActivityTotal() {
        return new ArrayList<>(repositoryActivityTotal);
    }

    public void setRepositoryActivityTotal(List<AggregateEventData.RepositoryData> repositoryActivityTotal) {
        this.repositoryActivityTotal = repositoryActivityTotal;
    }

    public Map<String, Integer> getPushesPerRepo() {
        return pushesPerRepo;
    }

    public void setPushesPerRepo(Map<String, Integer> pushesPerRepo) {
        this.pushesPerRepo = pushesPerRepo;
    }

    public Map<String, Double> getActivitiesPercentage() {
        return activitiesPercentage;
    }

    public void setActivitiesPercentage(Map<String, Double> activitiesPercentage) {
        this.activitiesPercentage = activitiesPercentage;
    }
}
