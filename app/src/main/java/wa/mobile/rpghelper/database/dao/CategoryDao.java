package wa.mobile.rpghelper.database.dao;

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

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Query("SELECT * FROM category WHERE insertable = 1")
    List<Category> getAllInsertable();

    @Transaction
    @Query("SELECT * FROM category ORDER BY id")
    List<CategoryWithCharacters> getAllWithCharacters();

    @Query("SELECT * FROM category WHERE id = :id")
    Category get(int id);

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM category WHERE id = :id")
    void delete(int id);
}
