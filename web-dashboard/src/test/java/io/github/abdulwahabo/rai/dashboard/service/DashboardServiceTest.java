package io.github.abdulwahabo.rai.dashboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.abdulwahabo.rai.dashboard.dao.AggregateEventDataDao;
import io.github.abdulwahabo.rai.dashboard.dao.AggregateEventDataDaoImpl;
import io.github.abdulwahabo.rai.dashboard.model.AggregateEventData;
import io.github.abdulwahabo.rai.dashboard.model.DashboardDataDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DashboardServiceTest {

    private AggregateEventDataDao dataDao = mock(AggregateEventDataDaoImpl.class);
    private String startDate = "2020-3-3";
    private String endDate = "2020-3-2";

    @BeforeEach
    public void setup() {
        when(dataDao.get(startDate, endDate)).thenReturn(Optional.of(mockData()));
    }

    @Test
    public void shouldReturnCorrectStats() {
        DashboardService service = new DashboardService(dataDao);
        DashboardDataDto dataDto = service.getData(startDate, endDate);
        Map<String, Double> percentages = dataDto.getActivitiesPercentage();

        assertEquals(26.4, percentages.get("forks"));
        assertEquals(15.9, percentages.get("watchers"));
        assertEquals(5.7, percentages.get("branches"));

        Map<String, Integer> pushes = dataDto.getPushesPerRepo();

        int repo2pushes = pushes.get("repo_2");
        int repo1pushes = pushes.get("repo_1");

        assertEquals(36, repo2pushes);
        assertEquals(27, repo1pushes);
    }

    private List<AggregateEventData> mockData() {

        AggregateEventData.RepositoryData repo1 = new AggregateEventData.RepositoryData();
        repo1.setComments(45);
        repo1.setWatcher(23);
        repo1.setForks(38);
        repo1.setPushes(15);
        repo1.setBranches(2);
        repo1.setRepository("repo_1");

        AggregateEventData.RepositoryData repo2 = new AggregateEventData.RepositoryData();
        repo2.setComments(42);
        repo2.setWatcher(13);
        repo2.setForks(21);
        repo2.setPushes(19);
        repo2.setBranches(6);
        repo2.setRepository("repo_2");

        AggregateEventData data1 = new AggregateEventData();
        data1.setDate("2020-3-2"); // not important
        data1.setRepositoryData(Arrays.asList(repo1, repo2));

        AggregateEventData.RepositoryData repo3 = new AggregateEventData.RepositoryData();
        repo3.setComments(41);
        repo3.setWatcher(20);
        repo3.setForks(32);
        repo3.setPushes(12);
        repo3.setBranches(4);
        repo3.setRepository("repo_1");

        AggregateEventData.RepositoryData repo4 = new AggregateEventData.RepositoryData();
        repo4.setComments(2);
        repo4.setWatcher(3);
        repo4.setForks(7);
        repo4.setPushes(17);
        repo4.setBranches(9);
        repo4.setRepository("repo_2");

        AggregateEventData data2 = new AggregateEventData();
        data2.setDate("2020-3-3");
        data2.setRepositoryData(Arrays.asList(repo3, repo4));

        return Arrays.asList(data1, data2);
    }
}
