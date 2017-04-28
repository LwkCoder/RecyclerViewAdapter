package com.lwksample.rcvadapter;

/**
 * Created by LWK
 * TODO
 * 2017/4/28
 */

public class TestSection
{
    private String section;

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

    @Override
    public String toString()
    {
        return "TestSection{" +
                "section='" + section + '\'' +
                '}';
    }
}
