package com.newsapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.newsapp.db.dao.NewsDao;
import com.newsapp.db.entities.Article;

@Database(entities = {Article.class}, version = 1)
abstract public class NewsDatabase extends RoomDatabase {

    private static NewsDatabase sInstance;

    public static NewsDatabase getInstance(Context context) {
        if (sInstance==null){
            synchronized (NewsDatabase.class){
                sInstance = Room.databaseBuilder(context.getApplicationContext()
                        , NewsDatabase.class, "News.db")
                        .fallbackToDestructiveMigration()
                        .build();

            }
        }
        return sInstance;
    }

    public abstract NewsDao getNewsDao();

}
