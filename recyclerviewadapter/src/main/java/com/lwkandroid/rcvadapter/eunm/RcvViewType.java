package com.lwkandroid.rcvadapter.eunm;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public final class RcvViewType
{
    //HeaderView的ViewType基础标识
    public static final int HEADER = 1000000;
    //FooterView的ViewType基础标识
    public static final int FOOTER = 2000000;
    //EmptyView的ViewType标识
    public static final int EMPTY = Integer.MAX_VALUE - 1;
    //LoadMore的ViewType标识
    public static final int LOAD_MORE = Integer.MAX_VALUE - 2;
    //Section标签的ViewType标识
    public static final int SECTION_LABEL = Integer.MAX_VALUE - 3;
}
