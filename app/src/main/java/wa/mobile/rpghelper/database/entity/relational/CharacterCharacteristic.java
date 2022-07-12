package wa.mobile.rpghelper.database.entity.relational;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;
import wa.mobile.rpghelper.database.entity.Characteristic;

public class CharacterCharacteristic {
    @Embedded
    public CharacterCharacteristicLink link;

    @Relation(parentColumn = "characteristic_id", entityColumn = "id")
    public Characteristic characteristic;

    public float getValue() { return link.getValue(); }
}
