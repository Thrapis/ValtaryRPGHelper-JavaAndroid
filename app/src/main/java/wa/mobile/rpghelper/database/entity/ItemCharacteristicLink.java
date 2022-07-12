package wa.mobile.rpghelper.database.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "item_characteristic_link",
        foreignKeys = {
            @ForeignKey(
                entity = Item.class,
                parentColumns = "id",
                childColumns = "item_id",
                onDelete = CASCADE
            ),
            @ForeignKey(
                entity = Characteristic.class,
                parentColumns = "id",
                childColumns = "characteristic_id",
                onDelete = CASCADE
            )
        },
        indices = {
                @Index("item_id"),
                @Index("characteristic_id"),
                @Index(value = {"item_id", "characteristic_id"}, unique = true)
        })

public class ItemCharacteristicLink {
    
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "item_id")
    private int itemId;
    @ColumnInfo(name = "characteristic_id")
    private int characteristicId;
    @ColumnInfo(name = "characteristic_value")
    private float value;

    public ItemCharacteristicLink() {}

    @Ignore
    public ItemCharacteristicLink(int itemId, int characteristicId, float value) {
        this.itemId = itemId;
        this.characteristicId = characteristicId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getItemId() {
        return itemId;
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

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setCharacteristicId(int characteristicId) {
        this.characteristicId = characteristicId;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
