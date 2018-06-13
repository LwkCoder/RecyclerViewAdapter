package com.lwksample.rcvadapter.refresh;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LWK
 * TODO 数据层
 */

public class DataModel implements RefreshConstract.Model
{
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void requestData(final int page, final int num, final CallBack<List<String>> callBack)
    {
        //模拟网络请求数据
        //前2页返回足够数据，第三页返回一半数据
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (page <= 2)
                {
                    callBack.onSuccess(createData(num));
                } else
                {
                    callBack.onSuccess(createData(num / 2));
                }
            }
        }, 2000);
    }

    private List<String> createData(int num)
    {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < num; i++)
        {
            list.add("数据" + i);
        }
        return list;
    }
}
