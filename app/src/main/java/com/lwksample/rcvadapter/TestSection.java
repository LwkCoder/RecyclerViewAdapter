package com.lwksample.rcvadapter;

/**
 * Created by LWK
 * TODO
 * 2017/4/28
 */

public class TestSection
{
    private String section;
    private int type;

    public TestSection(String section)
    {
        this.section = section;
    }

    public String getSection()
    {
        return section;
    }

    public void setSection(String section)
    {
        this.section = section;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "TestSection{" +
                "section='" + section + '\'' +
                ", type=" + type +
                '}';
    }
}
