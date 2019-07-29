package com.team.androidfine.model.dao;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import io.reactivex.Completable;

public interface CudDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSync(T entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSync(T entity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(T entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(T entity);

    @Delete
    Completable delete(T entity);

}
