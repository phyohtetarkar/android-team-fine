package com.team.androidfine.model.repo.local;

import com.team.androidfine.model.dao.CategoryDao;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.model.repo.CategoryRepo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class CategoryRepoImpl implements CategoryRepo {

    private CategoryDao dao;

    public CategoryRepoImpl(CategoryDao dao) {
        this.dao = dao;
    }

    public Completable save(Category c) {
        if (c.getId() > 0) {
            return dao.update(c);
        }
        return dao.insert(c);
    }

    public Completable insert(Category c) {
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
