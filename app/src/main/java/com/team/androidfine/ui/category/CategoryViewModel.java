package com.team.androidfine.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.model.repo.CategoryRepo;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepo repo;
    private CompositeDisposable disposable = new CompositeDisposable();

    final MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    final MutableLiveData<Boolean> deleteResult = new MutableLiveData<>();

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).categoryRepo();
    }

    public void insert(Category category) {
        disposable.add(repo.insert(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::findCategories, t -> {
                }));
    }

    void findCategories() {
        disposable.add(repo.findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categories::setValue, t -> {
                    t.printStackTrace();
                }));
    }

    void delete(Category category) {
        disposable.add(repo.delete(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    deleteResult.setValue(true);
                    findCategories();
                }, t -> {
                    deleteResult.setValue(false);
                    findCategories();
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
