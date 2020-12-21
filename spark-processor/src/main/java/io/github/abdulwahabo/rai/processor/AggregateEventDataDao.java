package io.github.abdulwahabo.rai.processor;

import io.github.abdulwahabo.rai.processor.exception.DaoException;
import io.github.abdulwahabo.rai.processor.model.AggregateEventData;
import java.util.List;

public interface AggregateEventDataDao {
    void save(List<AggregateEventData> data) throws DaoException;
}

// TODO: this is good for unit testing... i can simply pass a DAO that does something
//      other than write to DynamoDB.