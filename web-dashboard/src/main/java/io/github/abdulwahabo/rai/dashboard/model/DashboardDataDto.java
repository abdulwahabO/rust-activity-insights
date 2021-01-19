package io.github.abdulwahabo.rai.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class DashboardDataDto {

    @JsonProperty("repo_issues_activity")
    private Map<String, Integer> topIssuesActivity;

    @JsonProperty("activities_percentage")
    private Map<String, Double> activitiesPercentage;

    public Map<String, Integer> getTopIssuesActivity() {
        return topIssuesActivity;
    }

    public void setTopIssuesActivity(Map<String, Integer> topIssuesActivity) {
        this.topIssuesActivity = topIssuesActivity;
    }

    public Map<String, Double> getActivitiesPercentage() {
        return activitiesPercentage;
    }

    public void setActivitiesPercentage(Map<String, Double> activitiesPercentage) {
        this.activitiesPercentage = activitiesPercentage;
    }
}
