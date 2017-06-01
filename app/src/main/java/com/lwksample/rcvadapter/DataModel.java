package com.lwksample.rcvadapter;

import com.lwkandroid.rcvadapter.bean.RcvSectionWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LWK
 * TODO
 * 2017/4/27
 */

public class DataModel
{
    public static List<TestData> getSomeTestData(String baseContent, int num)
    {
        List<TestData> list = new ArrayList<>();
        for (int i = 0; i < num; i++)
        {
            list.add(new TestData(baseContent + "i"));
        }
        return list;
    }

    public static List<RcvSectionWrapper<TestSection, TestData>> getSomeSectionData(int num)
    {
        List<RcvSectionWrapper<TestSection, TestData>> list = new ArrayList<>();
        for (int i = 0; i < num; i++)
        {
            if (i != 0 && i <= 60 && i % 4 == 0)
            {
                TestSection section = new TestSection("Label" + i);
                list.add(new RcvSectionWrapper<TestSection, TestData>(true, section, null));
            } else
            {
                TestData testData = new TestData("Item数据" + i);
                list.add(new RcvSectionWrapper<TestSection, TestData>(false, null, testData));
            }
        }
        return list;
    }

    public static List<RcvSectionWrapper<TestSection, TestData>> getSomeSectionData2(int num)
    {
        List<RcvSectionWrapper<TestSection, TestData>> list = new ArrayList<>();
        for (int i = 0; i < num; i++)
        {
            if (i != 0 && i <= 50 && i % 5 == 0)
            {
                TestSection section = new TestSection("新增Label" + i);
                list.add(new RcvSectionWrapper<TestSection, TestData>(true, section, null));
            } else
            {
                TestData testData = new TestData("新增Item数据" + i);
                list.add(new RcvSectionWrapper<TestSection, TestData>(false, null, testData));
            }
        }
        return list;
    }
}
