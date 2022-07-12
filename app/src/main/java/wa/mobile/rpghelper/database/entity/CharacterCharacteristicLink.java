package wa.mobile.rpghelper.database.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "character_characteristic_link",
        foreignKeys = {
            @ForeignKey(
                entity = Character.class,
                parentColumns = "id",
                childColumns = "character_id",
                onDelete = CASCADE),
            @ForeignKey(
                entity = Characteristic.class,
                parentColumns = "id",
                childColumns = "characteristic_id",
                onDelete = CASCADE
                )},
        indices = {
                @Index("character_id"),
                @Index("characteristic_id"),
                @Index(value = {"character_id", "characteristic_id"}, unique = true)
        }
)

public class CharacterCharacteristicLink {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "character_id")
    private int characterId;
    @ColumnInfo(name = "characteristic_id")
    private int characteristicId;
    @ColumnInfo(name = "characteristic_value")
    private float value;

    public CharacterCharacteristicLink() {}

    @Ignore
    public CharacterCharacteristicLink(int characterId, int characteristicId, float value) {
        this.characterId = characterId;
        this.characteristicId = characteristicId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getCharacteristicId() {
        return characteristicId;
    }

    public float getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public void setCharacteristicId(int characteristicId) {
        this.characteristicId = characteristicId;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
