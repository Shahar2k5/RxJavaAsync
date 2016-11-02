package com.bytesizebit.rxasync;

import com.bytesizebit.rxasync.interfaces.ErrorListener;
import com.bytesizebit.rxasync.interfaces.LifeCycleDisposable;
import com.bytesizebit.rxasync.interfaces.WorkDoneListener;
import com.bytesizebit.rxasync.interfaces.WorkDoneWithReturnListener;
import com.bytesizebit.rxasync.interfaces.WorkFunction;
import com.bytesizebit.rxasync.interfaces.WorkFunctionWithReturn;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Shahar Barsheshet on 02/11/2016.
 */

public class RxAsync
{
    public static void io(LifeCycleDisposable lifeCycleDisposable, WorkFunction func, WorkDoneListener listener, ErrorListener errorListener)
    {
        async(lifeCycleDisposable, func, listener, errorListener, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public static void io(LifeCycleDisposable lifeCycleDisposable, WorkFunction func, WorkDoneListener listener)
    {
        async(lifeCycleDisposable, func, listener, null, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public static void io(LifeCycleDisposable lifeCycleDisposable, WorkFunction func)
    {
        async(lifeCycleDisposable, func, null, null, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public static <T> void io(LifeCycleDisposable lifeCycleDisposable, WorkFunctionWithReturn<T> func, WorkDoneWithReturnListener<T> listener)
    {
        async(lifeCycleDisposable, func, listener, null, Schedulers.io(), AndroidSchedulers.mainThread());
    }

    public static void computation(LifeCycleDisposable lifeCycleDisposable, WorkFunction func, WorkDoneListener listener)
    {
        async(lifeCycleDisposable, func, listener, null, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    public static void computation(LifeCycleDisposable lifeCycleDisposable, WorkFunction func, WorkDoneListener listener, ErrorListener errorListener)
    {
        async(lifeCycleDisposable, func, listener, errorListener, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    public static void computation(LifeCycleDisposable lifeCycleDisposable, WorkFunction func)
    {
        async(lifeCycleDisposable, func, null, null, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    public static <T> void computation(LifeCycleDisposable lifeCycleDisposable, WorkFunctionWithReturn<T> func, WorkDoneWithReturnListener<T> listener)
    {
        async(lifeCycleDisposable, func, listener, null, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    public static <T> void computation(LifeCycleDisposable lifeCycleDisposable, WorkFunctionWithReturn<T> func, WorkDoneWithReturnListener<T> listener, ErrorListener errorListener)
    {
        async(lifeCycleDisposable, func, listener, errorListener, Schedulers.computation(), AndroidSchedulers.mainThread());
    }

    public static <T> void async(
            LifeCycleDisposable lifeCycleDisposable, final WorkFunctionWithReturn<T> func, final WorkDoneWithReturnListener<T> doneListener, final ErrorListener errorListener, Scheduler
            workerScheduler, Scheduler resultScheduler)
    {
        Disposable disposable = Observable
                .defer(new Callable<ObservableSource<T>>()
                {
                    @Override
                    public ObservableSource<T> call() throws Exception
                    {
                        return Observable.just(func.call());
                    }
                })
                .subscribeOn(workerScheduler)
                .observeOn(resultScheduler)
                .subscribe(new Consumer<T>()
                {
                    @Override
                    public void accept(T t) throws Exception
                    {
                        if (doneListener != null)
                        {
                            try
                            {
                                doneListener.onDone(t);
                            }
                            catch (Exception e)
                            {
                                errorListener.onError(e);
                            }
                        }
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        if (errorListener != null)
                        {
                            errorListener.onError(throwable);
                        }
                    }
                }, new Action()
                {
                    @Override
                    public void run() throws Exception
                    {
                    }
                });
        if (lifeCycleDisposable != null)
        {
            lifeCycleDisposable.addToLifecycleDisposable(disposable);
        }
    }

    public static void async(LifeCycleDisposable lifeCycleDisposable, final WorkFunction func, final WorkDoneListener listener, final ErrorListener errorListener, Scheduler workerScheduler, Scheduler resultScheduler)
    {
        Disposable disposable = Observable
                .defer(new Callable<ObservableSource<?>>()
                {
                    @Override
                    public ObservableSource<?> call() throws Exception
                    {
                        try
                        {
                            func.call();
                            return Observable.empty();
                        }
                        catch (Exception e)
                        {
                            return Observable.error(e);
                        }
                    }
                })
                .subscribeOn(workerScheduler)
                .observeOn(resultScheduler)
                .subscribe(new Consumer<Object>()
                {
                    @Override
                    public void accept(Object o) throws Exception
                    {
                    }
                }, new Consumer<Throwable>()
                {
                    @Override
                    public void accept(Throwable throwable) throws Exception
                    {
                        if (errorListener != null)
                        {
                            errorListener.onError(throwable);
                        }
                    }
                }, new Action()
                {
                    @Override
                    public void run() throws Exception
                    {
                        if (listener != null)
                        {
                            try
                            {
                                listener.onDone();
                            }
                            catch (Exception e)
                            {
                                errorListener.onError(e);
                            }
                        }
                    }
                });
        if (lifeCycleDisposable != null)
        {
            lifeCycleDisposable.addToLifecycleDisposable(disposable);
        }
    }

    public static Observable<Void> asObservable(final WorkFunction func)
    {
        return Observable
                .defer(new Callable<ObservableSource<? extends Void>>()
                {
                    @Override
                    public ObservableSource<? extends Void> call() throws Exception
                    {
                        func.call();
                        return Observable.just(null);
                    }
                });
    }

    public static <T> Observable<T> asObservable(final WorkFunctionWithReturn<T> func)
    {
        return Observable
                .defer(new Callable<ObservableSource<? extends T>>()
                {
                    @Override
                    public ObservableSource<? extends T> call() throws Exception
                    {
                        return Observable.just(func.call());
                    }
                });
    }
}
