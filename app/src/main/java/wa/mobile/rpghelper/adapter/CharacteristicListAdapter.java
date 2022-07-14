package wa.mobile.rpghelper.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.Characteristic;
import wa.mobile.rpghelper.database.entity.relational.CharacterCharacteristic;
import wa.mobile.rpghelper.util.ContextMenuType;

public class CharacteristicListAdapter extends BaseAdapter {
    Context _context;
    LayoutInflater _inflater;
    List<Characteristic> _objects;

    public interface OnChoose {
        void run(int itemId);
    }

    OnChoose _doOnChoose;

    public CharacteristicListAdapter(Context context, List<Characteristic> characteristics, OnChoose onChoose) {
        _context = context;
        _objects = characteristics;
        _inflater = (LayoutInflater) _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _doOnChoose = onChoose;
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
        view.findViewById(R.id.choose_button).setOnClickListener(clickedView -> {
            _doOnChoose.run(c.getId());
        });

        view.setTag(R.id.context_menu_type, ContextMenuType.CHARACTERISTIC);
        view.setTag(R.id.item_id, c.getId());
        ((Activity)_context).registerForContextMenu(view);

        return view;
    }
}