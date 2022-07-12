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
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;
import wa.mobile.rpghelper.database.entity.relational.ItemWithCharacteristics;

@Dao
public interface ItemDao {

    @Query("SELECT * FROM item")
    List<Item> getAll();

    @Query("SELECT * FROM item WHERE id = :id")
    Item get(int id);

    @Transaction
    @Query("SELECT * FROM item WHERE id = :id")
    ItemWithCharacteristics getItemWithCharacteristics(int id);

    @Insert
    void insert(Item item);

    @Update
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("DELETE FROM item WHERE id = :id")
    void delete(int id);
}
