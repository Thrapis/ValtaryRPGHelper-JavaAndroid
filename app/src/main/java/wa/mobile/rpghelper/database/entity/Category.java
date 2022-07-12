package wa.mobile.rpghelper.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "insertable")
    private boolean insertable;
    @Embedded
    public EntityImage image;

    public Category() {}

    @Ignore
    public Category(String name, boolean insertable, EntityImage image){
        this.name = name; this.insertable = insertable; this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " category";
    }

}
