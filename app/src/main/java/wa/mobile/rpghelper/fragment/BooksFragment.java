package wa.mobile.rpghelper.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.localstore.entity.User;
import wa.mobile.rpghelper.localstore.manager.UserStoreManager;

public class BooksFragment extends Fragment {

    View inflatedView;

    public BooksFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_books, container, false);
        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View createdView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(createdView, savedInstanceState);
        ScrollView bookContentScroller = createdView.findViewById(R.id.book_content_scroller);
        TextView bookContent = createdView.findViewById(R.id.book_content);
        Spinner bookSpinner = createdView.findViewById(R.id.book_selection);

        String[] bookIds = getResources().getStringArray(R.array.book_select);
        ArrayList<String> bookNames = new ArrayList<String>();
        for (String bookId: bookIds) {
            int resId = getResources().getIdentifier(bookId, "string", getActivity().getPackageName());
            bookNames.add(getResources().getString(resId));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(createdView.getContext(), android.R.layout.simple_list_item_1, bookNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(adapter);
        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    int resId = getResources().getIdentifier(bookIds[position],
                            "raw", getActivity().getPackageName());
                    InputStream is = getResources().openRawResource(resId);
                    int size = is.available();
                    byte buffer[] = new byte[size];
                    is.read(buffer);
                    is.close();
                    bookContentScroller.scrollTo(0,0);
                    bookContent.setText(new String(buffer));

                    User user = UserStoreManager.getUser(createdView.getContext());
                    user.setLastBookSelection(position);
                    UserStoreManager.updateUser(createdView.getContext(), user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        User user = UserStoreManager.getUser(createdView.getContext());
        bookSpinner.setSelection(user.getLastBookSelection());
    }
}