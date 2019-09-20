package com.team.androidfine.model.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.team.androidfine.model.entity.Category;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CategoryDao extends CudDao<Category> {

    @Query("SELECT * FROM CATEGORY")
    Flowable<List<Category>> findAll();

    @Query("SELECT * FROM CATEGORY WHERE id = :id LIMIT 1")
    Single<Category> findById(int id);
}
