package com.lwksample.rcvadapter.refresh;

import java.util.List;

/**
 * Created by LWK
 * TODO
 */

public interface RefreshConstract
{
    interface View
    {
        void onRefreshSuccess(List<String> dataList, boolean hasMoreData);

        void onRefreshFail();

        void onLoadMoreSuccess(List<String> dataList, boolean hasMoreData);

        void onLoadMoreFail();
    }

    interface Model
    {
        void requestData(int page, int num, CallBack<List<String>> callBack);
    }

    interface Presenter
    {
        void requestData(boolean isRrefresh, int page, int num);
    }
}
