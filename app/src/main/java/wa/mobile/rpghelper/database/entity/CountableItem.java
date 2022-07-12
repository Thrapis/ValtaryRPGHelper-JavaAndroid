package wa.mobile.rpghelper.database.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "countable_item",
        foreignKeys = @ForeignKey(
                entity = Character.class,
                parentColumns = "id",
                childColumns = "character_id",
                onDelete = CASCADE),
        indices = {
                @Index("character_id"),
        })

public class CountableItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "character_id")
    private int characterId;
    @ColumnInfo(name = "name")
    private String name;
    @Embedded
    public EntityImage image;
    @ColumnInfo(name = "count")
    private int count;

    public CountableItem() {}

    @Ignore
    public CountableItem(String name, int characterId, EntityImage image, int startCount) {
        this.name = name;
        this.characterId = characterId;
        this.image = image;
        this.count = startCount;
    }

    public int getId() {
        return id;
    }

    public int getCharacterId() {
        return characterId;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
