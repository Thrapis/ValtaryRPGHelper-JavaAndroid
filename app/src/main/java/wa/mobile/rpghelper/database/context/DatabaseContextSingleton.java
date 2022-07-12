package wa.mobile.rpghelper.database.context;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.EntityImage;

public class DatabaseContextSingleton {
    private static DatabaseContext database;

    public static DatabaseContext getDatabaseContext(Context context) {

        if (database == null) {
            boolean[] doImport = { false };
            database = Room
                    .databaseBuilder(context, DatabaseContext.class, "database")
                    .allowMainThreadQueries()
                    .addCallback(new RoomDatabase.Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            doImport[0] = true;
                        }
                    })
                    .build();
            database.categoryDao().getAll();
            if (doImport[0]) {
                String player_n = context.getResources().getString(R.string.cat_player);
                String non_player_n = context.getResources().getString(R.string.cat_non_player);
                String imported_n = context.getResources().getString(R.string.cat_imported);
                database.categoryDao().insert(new Category(player_n, true,
                        new EntityImage(context, R.drawable.cat_player)));
                database.categoryDao().insert(new Category(non_player_n, true,
                        new EntityImage(context, R.drawable.cat_non_player)));
                database.categoryDao().insert(new Category(imported_n, false,
                        new EntityImage(context, R.drawable.cat_imported)));
            }
        }

        return database;
    }
}
