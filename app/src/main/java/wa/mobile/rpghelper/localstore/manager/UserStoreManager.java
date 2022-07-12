package wa.mobile.rpghelper.localstore.manager;

import android.content.Context;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import wa.mobile.rpghelper.localstore.entity.User;

public class UserStoreManager {

    public static final String USER_FILE_NAME = "user.json";


    public static boolean updateUser(Context context, User user) {

        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(USER_FILE_NAME, Context.MODE_PRIVATE);
            outputStream.write(jsonString.getBytes());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static User getUser(Context context) {

        InputStreamReader streamReader = null;
        FileInputStream inputStream = null;
        try {
            inputStream = context.openFileInput(USER_FILE_NAME);
            streamReader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            User user = gson.fromJson(streamReader, User.class);
            //handleListeners();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new User();
    }
}
