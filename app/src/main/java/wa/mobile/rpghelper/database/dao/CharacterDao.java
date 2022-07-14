package wa.mobile.rpghelper.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

import wa.mobile.rpghelper.database.converter.UUIDTypeConverter;
import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.relational.CategoryWithCharacters;
import wa.mobile.rpghelper.database.entity.relational.CharacterInfo;

@Dao
public interface CharacterDao {

    @Query("SELECT * FROM character")
    List<Character> getAll();

    @Query("SELECT * FROM character WHERE category_id = :categoryId")
    List<Character> getAllOfCategory(int categoryId);

    @Query("SELECT * FROM character WHERE id = :id")
    Character get(int id);

    @Transaction
    @Query("SELECT * FROM character WHERE id = :id")
    CharacterInfo getInfo(int id);

    @Insert
    void insert(Character character);

    @Update
    void update(Character character);

    @Delete
    void delete(Character character);

    @Query("DELETE FROM character WHERE id = :id")
    void delete(int id);
}
