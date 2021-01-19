package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DaoException;
import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import java.util.List;

public interface AggregateEventDataDao {
    void save(List<AggregateEventData> data) throws DaoException;
}
