package wa.mobile.rpghelper.database.entity.relational;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.List;

import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.ItemCharacteristicLink;

public class ItemWithCharacteristics {
    @Embedded
    public Item item;
    @Relation(entity = ItemCharacteristicLink.class, parentColumn = "id", entityColumn = "item_id")
    public List<ItemCharacteristic> itemCharacteristics;
}
