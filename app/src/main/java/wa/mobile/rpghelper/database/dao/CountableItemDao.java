package wa.mobile.rpghelper.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.CountableItem;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;

@Dao
public interface CountableItemDao {

    @Query("SELECT * FROM countable_item")
    List<CountableItem> getAll();

    @Query("SELECT * FROM countable_item WHERE id = :id")
    CountableItem get(int id);

    @Insert
    void insert(CountableItem countableItem);

    @Update
    void update(CountableItem countableItem);

    @Delete
    void delete(CountableItem countableItem);

    @Query("DELETE FROM countable_item WHERE id = :id")
    void delete(int id);
}
