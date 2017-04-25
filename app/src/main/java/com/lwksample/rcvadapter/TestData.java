package com.lwksample.rcvadapter;

/**
 * Created by LWK
 * TODO
 * 2017/4/25
 */

public class TestData
{
    private String content;

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

    @Override
    public String toString()
    {
        return "TestData{" +
                "content='" + content + '\'' +
                '}';
    }
}
