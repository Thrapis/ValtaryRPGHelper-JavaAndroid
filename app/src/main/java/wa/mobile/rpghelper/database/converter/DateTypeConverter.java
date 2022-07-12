package wa.mobile.rpghelper.database.converter;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateTypeConverter {

    @TypeConverter
    public static Long fromDate(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return new Date(timestamp);
    }

}