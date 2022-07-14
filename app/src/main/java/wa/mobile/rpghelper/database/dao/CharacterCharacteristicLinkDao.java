package wa.mobile.rpghelper.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;

@Dao
public interface CharacterCharacteristicLinkDao {

    @Query("SELECT * FROM character_characteristic_link")
    List<CharacterCharacteristicLink> getAll();

    @Query("SELECT * FROM character_characteristic_link WHERE id = :id")
    CharacterCharacteristicLink get(int id);

    @Query("SELECT * FROM character_characteristic_link WHERE " +
            "character_id = :characterId AND characteristic_id = :characteristicId")
    CharacterCharacteristicLink get(int characterId, int characteristicId);

    @Query("SELECT id FROM character_characteristic_link WHERE " +
            "character_id = :characterId AND characteristic_id = :characteristicId")
    int getId(int characterId, int characteristicId);

    @Insert
    void insert(CharacterCharacteristicLink link);

    @Update
    void update(CharacterCharacteristicLink link);

    @Delete
    void delete(CharacterCharacteristicLink link);

    @Query("DELETE FROM character_characteristic_link WHERE id = :id")
    void delete(int id);
}
