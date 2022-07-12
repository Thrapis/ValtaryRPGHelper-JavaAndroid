package wa.mobile.rpghelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import wa.mobile.rpghelper.R;
import wa.mobile.rpghelper.activity.CharacterProfileActivity;
import wa.mobile.rpghelper.database.entity.Category;
import wa.mobile.rpghelper.database.entity.Character;
import wa.mobile.rpghelper.database.entity.relational.CategoryWithCharacters;
import wa.mobile.rpghelper.util.IntentKey;

public class CategoryExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context _context;
    private List<CategoryWithCharacters> _listData;

    public CategoryExpandableListAdapter(Context context, List<CategoryWithCharacters> listData) {
        _context = context;
        _listData = listData;
    }

    public void setListData(List<CategoryWithCharacters> listData) {
        _listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public Character getChild(int groupPosition, int childPosition) {
        return _listData.get(groupPosition).characters.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Character character = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_ext_item_character, null);
        }

        ImageView imageView = convertView.findViewById(R.id.character_image);
        TextView nameTextView = convertView.findViewById(R.id.character_name);
        TextView levelTextView = convertView.findViewById(R.id.character_level);

        imageView.setImageBitmap(character.image.getBitmap(_context));
        nameTextView.setText(character.getName());
        levelTextView.setText(Integer.toString(character.getLevel()));

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(_context, CharacterProfileActivity.class);
            intent.putExtra(IntentKey.CHARACTER_ID, character.getId());
            _context.startActivity(intent);
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return _listData.get(groupPosition).characters.size();
    }

    @Override
    public Category getGroup(int groupPosition) {
        return this._listData.get(groupPosition).category;
    }

    @Override
    public int getGroupCount() {
        return _listData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        Category category = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_ext_group_category, null);
        }

        ImageView imageView = convertView.findViewById(R.id.category_image);
        TextView nameTextView = convertView.findViewById(R.id.category_name);

        imageView.setImageBitmap(category.image.getBitmap(_context));
        nameTextView.setText(category.getName());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}