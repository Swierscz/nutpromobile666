package pl.wat.nutpromobile.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import pl.wat.nutpromobile.db.row.UserRow;

@Dao
public interface UserDao {

    @Insert
    void insert(UserRow user);

    @Query("SELECT * FROM userrow WHERE login = :login")
    UserRow findUserByLogin(String login);
}
