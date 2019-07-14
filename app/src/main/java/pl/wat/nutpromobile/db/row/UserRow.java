package pl.wat.nutpromobile.db.row;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Entity
@Data
public class UserRow {

    @PrimaryKey
    public int uid;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "login")
    private String login;
}
