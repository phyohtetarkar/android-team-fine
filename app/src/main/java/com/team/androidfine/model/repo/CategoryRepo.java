package com.team.androidfine.model.repo;

import com.team.androidfine.model.dao.CategoryDao;
import com.team.androidfine.model.entity.Category;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class CategoryRepo {

    private CategoryDao dao;

    public CategoryRepo(CategoryDao dao) {
        this.dao = dao;
    }

    public Completable save(Category c) {
        if (c.getId() > 0) {
            return dao.update(c);
        }
        return dao.insert(c);
    }

    public Completable delete(Category c) {
        return dao.delete(c);
    }

    public Flowable<List<Category>> findAll() {
        return dao.findAll();
    }

    public Single<Category> findById(int id) {
        return dao.findById(id);
    }
}
