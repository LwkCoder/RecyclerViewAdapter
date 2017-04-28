package com.lwkandroid.rcvadapter.bean;

/**
 * Created by LWK
 * TODO 包装Section和Data数据的类
 * 2017/4/28
 */
public class RcvSectionWrapper<S, D>
{
    private boolean isSection;

    private S section;

    private D data;

    public RcvSectionWrapper(boolean isSection, S section, D data)
    {
        this.isSection = isSection;
        this.section = section;
        this.data = data;
    }

    public boolean isSection()
    {
        return isSection;
    }

    public void setSection(boolean section)
    {
        isSection = section;
    }

    public S getSection()
    {
        return section;
    }

    public void setSection(S section)
    {
        this.section = section;
    }

    public D getData()
    {
        return data;
    }

    public void setData(D data)
    {
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "RcvSectionWrapper{" +
                "isSection=" + isSection +
                ", section=" + section +
                ", data=" + data +
                '}';
    }
}
