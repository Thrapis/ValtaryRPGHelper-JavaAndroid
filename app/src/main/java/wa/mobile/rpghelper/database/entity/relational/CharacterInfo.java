package wa.mobile.rpghelper.database.entity.relational;

import androidx.core.util.Pair;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.CountableItem;
import wa.mobile.rpghelper.database.entity.Item;

public class CharacterInfo {
    @Embedded
    public Character character;

    @Relation(parentColumn = "category_id", entityColumn = "id")
    public Category category;

    @Relation(parentColumn = "id", entityColumn = "character_id")
    public List<Ability> abilities;
    @Relation(entity = CharacterCharacteristicLink.class, parentColumn = "id", entityColumn = "character_id")
    public List<CharacterCharacteristic> characteristics;

    @Relation(parentColumn = "id", entityColumn = "character_id")
    public List<CountableItem> countableItems;
    @Relation(entity = Item.class, parentColumn = "id", entityColumn = "character_id")
    public List<ItemWithCharacteristics> items;

    public int[] getCharacterCharacteristicsIds() {
        int[] result = new int[characteristics.size()];
        for (int i = 0; i < characteristics.size(); i++) {
            result[i] = characteristics.get(i).characteristic.getId();
        }
        return result;
    }

    public HashMap<Characteristic, Pair<Float, Float>> getCharacteristicSummary() {
        HashMap<Characteristic, Pair<Float, Float>> summary = new HashMap<>();
        for (CharacterCharacteristic charChar: characteristics) {
            if (summary.containsKey(charChar.characteristic)) {
                Pair<Float, Float> current = summary.get(charChar.characteristic);
                Pair<Float, Float> newCurrent =
                        new Pair<>(current.first + charChar.getValue(),
                                current.second + charChar.getValue());
                summary.put(charChar.characteristic, newCurrent);
            } else {
                summary.put(charChar.characteristic,
                        new Pair<>(charChar.getValue(), charChar.getValue()));
            }
        }
        for (ItemWithCharacteristics iwc: items) {
            for (ItemCharacteristic itemChar: iwc.itemCharacteristics) {
                if (summary.containsKey(itemChar.characteristic)) {
                    Pair<Float, Float> current = summary.get(itemChar.characteristic);
                    Pair<Float, Float> newCurrent =
                            new Pair<>(current.first,
                                    current.second + itemChar.getValue());
                    summary.put(itemChar.characteristic, newCurrent);
                } else {
                    summary.put(itemChar.characteristic,
                            new Pair<>(0f, itemChar.getValue()));
                }
            }
        }
        return summary;
    }
}
