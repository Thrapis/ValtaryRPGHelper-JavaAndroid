package wa.mobile.rpghelper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import wa.mobile.rpghelper.database.context.DatabaseContext;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.dao.CharacterCharacteristicLinkDao;
import wa.mobile.rpghelper.database.dao.CharacterDao;
import wa.mobile.rpghelper.database.dao.CharacteristicDao;
import wa.mobile.rpghelper.database.dao.ItemCharacteristicLinkDao;
import wa.mobile.rpghelper.database.dao.ItemDao;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.CharacterCharacteristicLink;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.EntityImage;
import wa.mobile.rpghelper.database.entity.Item;
import wa.mobile.rpghelper.database.entity.ItemCharacteristicLink;
import wa.mobile.rpghelper.fragment.BooksFragment;
import wa.mobile.rpghelper.fragment.CharacterFragment;
import wa.mobile.rpghelper.fragment.MapFragment;
import wa.mobile.rpghelper.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    String navSelectionKey = "nav_selection";
    int navSelection = R.id.characters;

    CharacterFragment characterFragment = new CharacterFragment();
    MapFragment mapFragment = new MapFragment();
    BooksFragment booksFragment = new BooksFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState != null && savedInstanceState.containsKey(navSelectionKey)){
            navSelection = savedInstanceState.getInt(navSelectionKey);
        }
        bottomNavigationView.setSelectedItemId(navSelection);

        DatabaseContext databaseContext = DatabaseContextSingleton.getDatabaseContext(this);
        CharacterDao characterDao = databaseContext.characterDao();
        ItemDao itemDao = databaseContext.itemDao();
        CharacteristicDao characteristicDao = databaseContext.characteristicDao();
        CharacterCharacteristicLinkDao characterLinkDao
                = databaseContext.characterCharacteristicLinkDao();
        ItemCharacteristicLinkDao itemLinkDao
                = databaseContext.itemCharacteristicLinkDao();

        Character character = characterDao.get(1);
        if (character == null) {
            characterDao.insert(new Character("Test Player", 1, new EntityImage(this, R.drawable.char_3)));
            character = characterDao.get(1);

            Item item1 = new Item("Item1", character.getId(),
                    new EntityImage(this, R.drawable.weapon_3), "Вроде щит");
            item1.setEquipped(true);
            itemDao.insert(item1);
            Item item2 = new Item("Item2", character.getId(),
                    new EntityImage(this, R.drawable.armor_5), "");
            item1.setEquipped(true);
            itemDao.insert(item2);
            Item item3 = new Item("Item3", character.getId(),
                    new EntityImage(this, R.drawable.weapon_14), "");
            itemDao.insert(item3);
            Item item4 = new Item("Item4", character.getId(),
                    new EntityImage(this, R.drawable.helmet_8), "");
            itemDao.insert(item4);

            Characteristic characteristic_1 = new Characteristic("Сила");
            Characteristic characteristic_2 = new Characteristic("Ловкость");
            Characteristic characteristic_3 = new Characteristic("Интеллект");
            characteristicDao.insert(characteristic_1);
            characteristicDao.insert(characteristic_2);
            characteristicDao.insert(characteristic_3);

            CharacterCharacteristicLink charLink_1
                    = new CharacterCharacteristicLink(1, 1, 5.2f);
            CharacterCharacteristicLink charLink_2
                    = new CharacterCharacteristicLink(1, 3, 2.1f);
            characterLinkDao.insert(charLink_1);
            characterLinkDao.insert(charLink_2);

            ItemCharacteristicLink itemLink_1
                    = new ItemCharacteristicLink(1, 1, 1.5f);
            ItemCharacteristicLink itemLink_2
                    = new ItemCharacteristicLink(1, 2, 2f);
            ItemCharacteristicLink itemLink_3
                    = new ItemCharacteristicLink(3, 3, 3f);
            itemLinkDao.insert(itemLink_1);
            itemLinkDao.insert(itemLink_2);
            itemLinkDao.insert(itemLink_3);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(navSelectionKey, navSelection);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.characters:
                navSelection = R.id.characters;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, characterFragment).commit();
                return true;
            case R.id.map:
                navSelection = R.id.map;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, mapFragment).commit();
                return true;
            case R.id.books:
                navSelection = R.id.books;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, booksFragment).commit();
                return true;
        }
        return false;
    }
}