package wa.mobile.rpghelper.database.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.TypeConverters;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import wa.mobile.rpghelper.database.converter.UriTypeConverter;

public class EntityImage {

    @ColumnInfo(name = "image_uri")
    @TypeConverters(UriTypeConverter.class)
    private Uri imageUri;

    public EntityImage() {}

    @Ignore
    public EntityImage(Context context, int drawableId) {
        context.getPackageName();
        imageUri = Uri.parse(String.format("android.resource://%s/%s", context.getPackageName(), drawableId));
    }

    @Ignore
    public EntityImage(Context context, Bitmap bitmap) {
        FileOutputStream outputStream = null;
        try {
            String fileName = UUID.randomUUID().toString() + ".png";
            imageUri = Uri.parse(new File(fileName).getAbsolutePath());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapData = bos.toByteArray();
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(bitmapData);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri path) {
        this.imageUri = path;
    }

    @Ignore
    public Bitmap getBitmap(Context context) {
        try {
            imageUri.getEncodedPath();
            return MediaStore.Images.Media.
                            getBitmap(context.getContentResolver(), imageUri);
        } catch (IOException e) {
            return null;
        }
    }
}
