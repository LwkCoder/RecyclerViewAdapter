package com.lwksample.rcvadapter;

/**
 * Created by LWK
 * TODO
 * 2017/4/25
 */

public class TestData
{
    private String content;
    private int type;

    public TestData(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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
        return "TestData{" +
                "content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
