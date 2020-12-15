package io.github.abdulwahabo.rai.processor.model;


import java.util.HashMap;
import java.util.Map;

// TODO: move to common module
//       Annonate with DynamoDB specific classes.
public class OutputDataModel {

    private String date;
    private final Map<String, RepositoryData> repositoryStatsMap = new HashMap<>();

    // TODO: Getters and Setters....

    public static class RepositoryData {

        private String issues;
        private String watcher;
        private String branches;
        private String commits;
        private String pullRequests;

        // TODO::::::::::: getters and setters
    }

}
