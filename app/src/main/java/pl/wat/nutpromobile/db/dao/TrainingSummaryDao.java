package pl.wat.nutpromobile.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import pl.wat.nutpromobile.db.row.TrainingSummaryRow;
import pl.wat.nutpromobile.model.TrainingSummary;

@Dao
public interface TrainingSummaryDao {

    @Insert
    void insert(TrainingSummaryRow trainingSummaryRow);

    @Query("SELECT * FROM trainingsummaryrow")
    LiveData<List<TrainingSummaryRow>> getAll();
}
