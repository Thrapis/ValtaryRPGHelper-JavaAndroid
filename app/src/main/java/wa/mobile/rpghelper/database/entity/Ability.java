package wa.mobile.rpghelper.database.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "ability",
        foreignKeys = @ForeignKey(
                entity = Character.class,
                parentColumns = "id",
                childColumns = "character_id",
                onDelete = CASCADE),
        indices = {
                @Index("character_id"),
        })

public class Ability {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "character_id")
    private int characterId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;

    public Ability() {}

    @Ignore
    public Ability(String name, int characterId, String description) {
        this.name = name;
        this.characterId = characterId;
        this.description = description;
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

    public String getDescription() {
        return description;
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

    public void setDescription(String description) {
        this.description = description;
    }
}
