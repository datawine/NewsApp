package com.example.newsapp.model;
import java.sql.*;

/**
 * Created by leavan on 2017/9/11.
 */

public class MySqlite {
    MySqlite(){
        try{
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e)
        {

        }
    }
}
