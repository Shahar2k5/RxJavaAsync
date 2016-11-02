package com.bytesizebit.rxasync.interfaces;

/**
 * Created by Shahar Barsheshet on 02/11/2016.
 */

public interface WorkDoneWithReturnListener<T>
{
    void onDone(T t);
}
