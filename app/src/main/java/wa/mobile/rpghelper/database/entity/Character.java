package wa.mobile.rpghelper.database.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import wa.mobile.rpghelper.database.converter.DateTypeConverter;

@Entity(tableName = "character",
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = CASCADE),
        indices = {
                @Index("category_id"),
        })

public class Character {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "category_id")
    private int categoryId;
    @ColumnInfo(name = "level")
    private int level = 1;
    @TypeConverters(DateTypeConverter.class)
    @ColumnInfo(name = "creation_date")
    private Date creationDate = new Date(System.currentTimeMillis());
    @Embedded
    public EntityImage image;

    public Character() {}

    @Ignore
    public Character(String name, int categoryId, EntityImage image){
        this.name = name;
        this.categoryId = categoryId;
        this.image = image;
    }

    @Ignore
    public Character(String name, int categoryId){
        this.name = name;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getLevel() {
        return level;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name + " character";
    }
}
