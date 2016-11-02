package com.example.shaharbarsheshet.rxjavaasync;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bytesizebit.rxasync.RxAsync;
import com.bytesizebit.rxasync.interfaces.ErrorListener;
import com.bytesizebit.rxasync.interfaces.WorkDoneListener;
import com.bytesizebit.rxasync.interfaces.WorkFunction;

public class MainActivity extends BaseActivity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxAsync.io(this, new WorkFunction()
        {
            @Override
            public void call()
            {
                Log.d(TAG, "call started: ");
                try
                {
                    Thread.sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                Log.d(TAG, "call ended: ");
            }
        });

        RxAsync.io(this, new WorkFunction()
                {
                    @Override
                    public void call()
                    {
                        Log.d(TAG, "call started: ");
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "call ended: ");
                    }
                },
                new WorkDoneListener()
                {
                    @Override
                    public void onDone()
                    {
                        Log.d(TAG, "onDone: ");
                    }
                });


        RxAsync.io(this, new WorkFunction()
                {
                    @Override
                    public void call()
                    {
                        Log.d(TAG, "call started: ");
                        Toast.makeText(MainActivity.this, "someText", Toast.LENGTH_SHORT).show();
                    }
                },
                new WorkDoneListener()
                {
                    @Override
                    public void onDone()
                    {
                        Log.d(TAG, "onDone: ");
                    }
                },
                new ErrorListener()
                {
                    @Override
                    public void onError(Throwable throwable)
                    {
                        Log.e(TAG, "onError: ", throwable);
                    }
                });
    }
}
