package wa.mobile.rpghelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.relational.CharacterCharacteristic;

public class CharacteristicListAdapter extends BaseAdapter {
    Context _context;
    LayoutInflater _inflater;
    List<Characteristic> _objects;

    public CharacteristicListAdapter(Context context, List<Characteristic> characteristics) {
        _context = context;
        _objects = characteristics;
        _inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _objects.size();
    }

    @Override
    public Object getItem(int position) {
        return _objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = _inflater.inflate(R.layout.list_element_characteristic, parent, false);
        }

        Characteristic c = (Characteristic) getItem(position);

        ((TextView) view.findViewById(R.id.characteristic_name)).setText(c.getName());

        return view;
    }
}