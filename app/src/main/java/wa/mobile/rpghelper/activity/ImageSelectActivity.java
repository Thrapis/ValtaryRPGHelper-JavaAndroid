package wa.mobile.rpghelper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.Arrays;
import java.util.Objects;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.adapter.ImageCardAdapter;
import wa.mobile.rpghelper.util.IntentKey;
import wa.mobile.rpghelper.util.ItemsPresentation;

public class ImageSelectActivity extends AppCompatActivity {

    RecyclerView imageCardsContainer;
    ImageCardAdapter adapter;
    private final ItemsPresentation presentation = ItemsPresentation.GRID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IntentKey.CARD_IMAGES)) {
            imageCardsContainer = findViewById(R.id.image_card_container);
            String[] images = intent.getStringArrayExtra(IntentKey.CARD_IMAGES);
            loadImages(images);
        } else {
            System.out.println("No bundle to load images!!!!");
            finish();
        }
    }

    private void loadImages(String[] images) {
        adapter = new ImageCardAdapter(this, Arrays.asList(images), view -> {
            int resourceId = (int) view.getTag();
            Intent intent = new Intent();
            intent.putExtra(IntentKey.CARD_IMAGES_RESULT, resourceId);
            setResult(RESULT_OK, intent);
            finish();
        });

        if (presentation == ItemsPresentation.GRID) {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            imageCardsContainer.setLayoutManager(layoutManager);
        }
        else if (presentation == ItemsPresentation.LIST) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            imageCardsContainer.setLayoutManager(layoutManager);
        }

        imageCardsContainer.setAdapter(adapter);
    }
}