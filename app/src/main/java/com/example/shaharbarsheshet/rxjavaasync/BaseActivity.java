package com.example.shaharbarsheshet.rxjavaasync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bytesizebit.rxasync.interfaces.LifeCycleDisposable;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseActivity extends AppCompatActivity implements LifeCycleDisposable
{

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Disposable addToLifecycleDisposable(Disposable disposable)
    {
        mCompositeDisposable.add(disposable);
        return mCompositeDisposable;
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mCompositeDisposable.clear();
    }
}
