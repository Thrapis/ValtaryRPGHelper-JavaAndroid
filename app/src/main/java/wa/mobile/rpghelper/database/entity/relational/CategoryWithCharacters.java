package wa.mobile.rpghelper.database.entity.relational;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;

public class CategoryWithCharacters {
    @Embedded
    public Category category;
    @Relation(parentColumn = "id", entityColumn = "category_id")
    public List<Character> characters;
}
