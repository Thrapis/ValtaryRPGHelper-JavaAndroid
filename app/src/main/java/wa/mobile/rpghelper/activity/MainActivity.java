package wa.mobile.rpghelper.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

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