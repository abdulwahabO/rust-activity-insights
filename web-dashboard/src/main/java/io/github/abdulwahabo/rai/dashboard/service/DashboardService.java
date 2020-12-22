package io.github.abdulwahabo.rai.dashboard.service;

import static java.nio.file.Paths.get;

import io.github.abdulwahabo.rai.dashboard.dao.AggregateEventDataDao;
import io.github.abdulwahabo.rai.dashboard.model.AggregateEventData;
import io.github.abdulwahabo.rai.dashboard.model.DashboardDataDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

            List<AggregateEventData.RepositoryData> repositoriesData = new ArrayList<>();
            List<AggregateEventData> aggregateEventData = optionalList.get();

            for (AggregateEventData eventData: aggregateEventData) {
                List<AggregateEventData.RepositoryData> repositoryData = eventData.getRepositoryData();
                //
            }

            // TODO: combine all data for a repo into a single RepoData.
            //      then sort

            return null;
        } else {
            return new DashboardDataDto();
        }

    }

    // TODO: Use Comparator<T> for sorting to get top 5;
}
