package com.lwksample.rcvadapter.refresh;

/**
 * Created by LWK
 * TODO
 */

public abstract class CallBack<T>
{
    abstract void onSuccess(T t);

    abstract void onFail();
}
