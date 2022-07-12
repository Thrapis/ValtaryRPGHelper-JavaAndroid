package wa.mobile.rpghelper.database.context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import wa.mobile.rpghelper.database.dao.AbilityDao;
import wa.mobile.rpghelper.database.dao.CategoryDao;
import wa.mobile.rpghelper.database.dao.CharacterCharacteristicLinkDao;
import wa.mobile.rpghelper.database.dao.CharacterDao;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.dao.CountableItemDao;
import wa.mobile.rpghelper.database.dao.ItemCharacteristicLinkDao;
import wa.mobile.rpghelper.database.dao.ItemDao;
import wa.mobile.rpghelper.database.entity.Ability;
import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.CountableItem;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.ItemCharacteristicLink;

@Database(
        entities = {
                Category.class,
                Character.class,
                Ability.class,
                Item.class,
                CountableItem.class,
                Characteristic.class,
                CharacterCharacteristicLink.class,
                ItemCharacteristicLink.class
        },
        version = 8, exportSchema = false)

public abstract class DatabaseContext extends RoomDatabase{

    public abstract CategoryDao categoryDao();
    public abstract CharacterDao characterDao();
    public abstract AbilityDao abilityDao();

    public abstract ItemDao itemDao();
    public abstract CountableItemDao countableItemDao();

    public abstract CharacteristicDao characteristicDao();

    public abstract CharacterCharacteristicLinkDao characterCharacteristicLinkDao();
    public abstract ItemCharacteristicLinkDao itemCharacteristicLinkDao();
}
