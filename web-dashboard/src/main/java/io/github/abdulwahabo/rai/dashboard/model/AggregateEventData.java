package io.github.abdulwahabo.rai.dashboard.model;

import java.util.ArrayList;
import java.util.List;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class AggregateEventData {

    private String date;
    private List<RepositoryData> repositoryData = new ArrayList<>();

    public List<RepositoryData> getRepositoryData() {
        return new ArrayList<>(repositoryData);
    }

    public void setRepositoryData(List<RepositoryData> repositoryData) {
        this.repositoryData = repositoryData;
    }

    @DynamoDbPartitionKey
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @DynamoDbBean
    public static class RepositoryData {

        private String repository;
        private int forks;
        private int watcher;
        private int branches;
        private int pushes;
        private int comments;

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

        /**
         *  todo: this is PR review comments.
         * @return
         */
        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getForks() {
            return forks;
        }

        public void setForks(int forks) {
            this.forks = forks;
        }

        public int getPushes() {
            return pushes;
        }

        public void setPushes(int pushes) {
            this.pushes = pushes;
        }

        public int getBranches() {
            return branches;
        }

        public void setBranches(int branches) {
            this.branches = branches;
        }
    }
}
