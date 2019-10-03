package com.team.androidfine.model.repo;

import com.team.androidfine.model.entity.Category;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CategoryRepo {

    Completable save(Category c);

    Completable insert(Category c);

    Completable delete(Category c);

    Flowable<List<Category>> findAll();

    Single<Category> findById(int id);
}
