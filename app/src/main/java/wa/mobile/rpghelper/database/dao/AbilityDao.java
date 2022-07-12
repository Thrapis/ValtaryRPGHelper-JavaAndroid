package wa.mobile.rpghelper.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.Item;

@Dao
public interface AbilityDao {

    @Query("SELECT * FROM ability")
    List<Ability> getAll();

    @Query("SELECT * FROM ability WHERE id = :id")
    Ability get(int id);

    @Query("SELECT COUNT(*) FROM ability WHERE name = :name")
    boolean exist(String name);

    @Insert
    void insert(Ability ability);

    @Update
    void update(Ability ability);

    @Delete
    void delete(Ability ability);

    @Query("DELETE FROM ability WHERE id = :id")
    void delete(int id);
}
