package io.github.abdulwahabo.rai.processor.model;


import java.util.ArrayList;
import java.util.List;

// TODO: move to common module
//       Annonate with DynamoDB specific classes.
public class AggregateEventData {

    private String date;

    private List<RepositoryData> repositoryData = new ArrayList<>();

    public List<RepositoryData> getRepositoryData() {
        return new ArrayList<>(repositoryData);
    }

    public void setRepositoryData(List<RepositoryData> repositoryData) {
        this.repositoryData = repositoryData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static class RepositoryData {

        private String repository;
        private int issues;
        private int watcher;
        private int branches;
        private int commits;
        private int pullRequests;

        public int getWatcher() {
            return watcher;
        }

        public void setWatcher(int watcher) {
            this.watcher = watcher;
        }

        /**
         * Returns the name of the repository whose stats are represented by this object.
         */
        public String getRepository() {
            return repository;
        }

        public void setRepository(String repository) {
            this.repository = repository;
        }

        public int getPullRequests() {

            return pullRequests;
        }

        public void setPullRequests(int pullRequests) {
            this.pullRequests = pullRequests;
        }

        public int getIssues() {
            return issues;
        }

        public void setIssues(int issues) {
            this.issues = issues;
        }

        public int getCommits() {
            return commits;
        }

        public void setCommits(int commits) {
            this.commits = commits;
        }

        public int getBranches() {
            return branches;
        }

        public void setBranches(int branches) {
            this.branches = branches;
        }
    }

}
