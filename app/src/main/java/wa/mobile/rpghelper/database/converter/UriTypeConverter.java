package wa.mobile.rpghelper.database.converter;

import android.net.Uri;

import androidx.room.TypeConverter;

import java.util.Date;

public class UriTypeConverter {

    @TypeConverter
    public static String fromUri(Uri uri) {
        if (uri == null) {
            return null;
        } else {
            return uri.toString();
        }
    }

    @TypeConverter
    public static Uri toUri(String text) {
        return Uri.parse(text);
    }

}