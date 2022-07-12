package wa.mobile.rpghelper.database.entity.relational;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.ItemCharacteristicLink;

public class ItemCharacteristic {
    @Embedded
    public ItemCharacteristicLink link;

    @Relation(parentColumn = "characteristic_id", entityColumn = "id")
    public Characteristic characteristic;

    public float getValue() { return link.getValue(); }
}
