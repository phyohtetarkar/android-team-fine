package com.team.androidfine.ui.category;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.team.androidfine.ServiceLocator;
import com.team.androidfine.model.entity.Category;
import com.team.androidfine.model.repo.CategoryRepo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryEditViewModel extends AndroidViewModel {

    private CategoryRepo repo;

    private CompositeDisposable disposable = new CompositeDisposable();
    final MutableLiveData<Boolean> saveResult = new MutableLiveData<>();
    public final MutableLiveData<Category> category = new MutableLiveData<>();

    public CategoryEditViewModel(@NonNull Application application) {
        super(application);
        repo = ServiceLocator.getInstance(application).categoryRepo();
    }

    public void save() {
        disposable.add(repo.save(category.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    saveResult.setValue(true);
                }, t -> {
                    saveResult.setValue(false);
                }));
    }

    void findById(int id) {
        disposable.add(repo.findById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(category::setValue,
                        t -> {
                            category.setValue(new Category());
                        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
    }
}
