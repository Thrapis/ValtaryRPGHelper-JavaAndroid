package wa.mobile.rpghelper.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;
import wa.mobile.rpghelper.database.entity.relational.ItemCharacteristic;
import wa.mobile.rpghelper.database.entity.relational.ItemWithCharacteristics;

@Dao
public interface CharacteristicDao {

    @Query("SELECT * FROM characteristic")
    List<Characteristic> getAll();

    @Query("SELECT * FROM characteristic WHERE id NOT IN (:ids)")
    List<Characteristic> getAllExcept(int[] ids);

    @Query("SELECT * FROM characteristic WHERE id = :id")
    Characteristic get(int id);

    @Query("SELECT COUNT(*) FROM characteristic WHERE name = :name")
    boolean exist(String name);

    @Insert
    void insert(Characteristic characteristic);

    @Update
    void update(Characteristic characteristic);

    @Delete
    void delete(Characteristic characteristic);

    @Query("DELETE FROM characteristic WHERE id = :id")
    void delete(int id);
}
