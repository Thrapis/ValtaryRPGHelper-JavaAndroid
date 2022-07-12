package wa.mobile.rpghelper.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Objects;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.context.DatabaseContext;
import wa.mobile.rpghelper.database.context.DatabaseContextSingleton;
import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.EntityImage;
import wa.mobile.rpghelper.util.IntentKey;

public class CharacterAddActivity extends AppCompatActivity {

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null) {
                    int resourceId = data.getExtras().getInt(IntentKey.CARD_IMAGES_RESULT);
                    ImageView imageView = findViewById(R.id.character_image_field);
                    imageView.setTag(resourceId);
                    imageView.setImageDrawable(AppCompatResources.getDrawable(this, resourceId));
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_add);
        Objects.requireNonNull(getSupportActionBar()).hide();

        DatabaseContext databaseContext = DatabaseContextSingleton.getDatabaseContext(this);

        ImageView imageView = findViewById(R.id.character_image_field);
        EditText nameEditText = findViewById(R.id.character_name_field);
        Spinner categorySpinner = findViewById(R.id.character_category_field);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button submitButton = findViewById(R.id.submit_button);

        imageView.setOnClickListener(view1 -> {
                    Intent intent = new Intent(this, ImageSelectActivity.class);
                    intent.putExtra(IntentKey.CARD_IMAGES, getResources().getStringArray(R.array.character_images));
                    startForResult.launch(intent);
                });

        imageView.setTag(R.drawable.char_0);
        imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.char_0));

        ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                databaseContext.categoryDao().getAllInsertable());
        categorySpinner.setAdapter(adapter);

        cancelButton.setOnClickListener(view -> finish());

        submitButton.setOnClickListener(view -> {
            Category selectedCategory = (Category) categorySpinner.getSelectedItem();
            EntityImage image = new EntityImage(this, (int) imageView.getTag());
            Character newCharacter =
                    new Character(nameEditText.getText().toString(), selectedCategory.getId(), image);
            databaseContext.characterDao().insert(newCharacter);
            finish();
        });
    }
}