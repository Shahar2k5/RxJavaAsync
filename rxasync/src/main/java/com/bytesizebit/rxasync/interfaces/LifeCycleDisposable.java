package com.bytesizebit.rxasync.interfaces;

import io.reactivex.disposables.Disposable;

/**
 * Created by shaharbarsheshet on 02/11/2016.
 */

public interface LifeCycleDisposable
{
    Disposable addToLifecycleDisposable(Disposable disposable);
}
