package com.newsapp.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.newsapp.constants.Category;
import com.newsapp.constants.Country;
import com.newsapp.db.entities.Article;

import java.util.List;

import io.reactivex.Single;

@Dao
public abstract class NewsDao {

    public Single<long[]> insertCategoryArticles(List<Article> articles, Category category,Country country) {

        for (Article article : articles) {
            article.setCategory(category.toString());
            article.setCountry(country.toString());
        }
        return insertArticles(articles);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract Single<long[]> insertArticles(List<Article> articles);

    public Single<List<Article>> loadCategoryArticle(int pageNumber, int pageSize, Category category, Country country) {
        return loadPages((pageNumber - 1) * pageSize, pageSize, category.toString(),country.toString());
    }

    @Query("SELECT * FROM article WHERE category=:category AND country=:country LIMIT :offset,:pageSize")
    abstract Single<List<Article>> loadPages(int offset, int pageSize, String category,String country);


}
