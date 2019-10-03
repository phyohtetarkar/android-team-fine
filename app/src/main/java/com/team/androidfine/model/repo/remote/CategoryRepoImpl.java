package com.team.androidfine.model.repo.remote;

import com.team.androidfine.model.api.CategoryAPI;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.model.repo.CategoryRepo;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class CategoryRepoImpl implements CategoryRepo {

    private CategoryAPI api;

    public CategoryRepoImpl(CategoryAPI api) {
        this.api = api;
    }

    @Override
    public Completable save(Category c) {
        return Completable.create(source -> {
            api.save(c).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onComplete();
                } else {
                    source.onError(new RuntimeException("Cannot save category."));
                }
            }, source::onError);
        });
    }

    @Override
    public Completable insert(Category c) {
        return save(c);
    }

    @Override
    public Completable delete(Category c) {
        return Completable.create(source -> {
            api.delete(c.getId()).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onComplete();
                } else {
                    source.onError(new RuntimeException("Cannot delete category."));
                }
            }, source::onError);
        });
    }

    @Override
    public Flowable<List<Category>> findAll() {
        return Flowable.create(source -> {
            api.findAll().blockingSubscribe(resp -> {
                source.onNext(resp.body());
            }, source::onError);
        }, BackpressureStrategy.LATEST);
    }

    @Override
    public Single<Category> findById(int id) {
        return Single.create(source -> {
            api.findById(id).blockingSubscribe(resp -> {
                if (resp.code() == 200) {
                    source.onSuccess(resp.body());
                } else {
                    source.onError(new RuntimeException("Category not found."));
                }
            }, source::onError);
        });
    }
}
