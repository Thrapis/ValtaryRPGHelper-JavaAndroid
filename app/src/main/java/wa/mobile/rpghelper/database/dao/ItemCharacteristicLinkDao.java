package wa.mobile.rpghelper.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.ItemCharacteristicLink;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;

@Dao
public interface ItemCharacteristicLinkDao {

    @Query("SELECT * FROM item_characteristic_link")
    List<ItemCharacteristicLink> getAll();

    @Query("SELECT * FROM item_characteristic_link WHERE id = :id")
    ItemCharacteristicLink get(int id);

    @Insert
    void insert(ItemCharacteristicLink link);

    @Update
    void update(ItemCharacteristicLink link);

    @Delete
    void delete(ItemCharacteristicLink link);

    @Query("DELETE FROM item_characteristic_link WHERE id = :id")
    void delete(int id);
}
