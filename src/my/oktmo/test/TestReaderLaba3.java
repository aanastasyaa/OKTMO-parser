package my.oktmo.test;

import my.oktmo.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class TestReaderLaba3 {
    private OktmoData data;

    @Before
    public void before() {
        data=new OktmoData();
        OktmoReader reader=new OktmoReader();
        reader.readPlacesAndGroups("data-201710.csv",data);
    }

    @Test
    public void testData() {
        OktmoGroup group1= data.getCommonMap().get(data.getCommonMap().firstKey());
        OktmoGroup group2=data.getCommonMap().get(data.getCommonMap().lastKey());
        assertEquals("01000000000", String.format("%011d",group1.getCode()));
        assertEquals(OktmoLevel.Region, group1.getLevel());
        assertEquals("Муниципальные образования Алтайского края", group1.getName());
        assertEquals("99701000000", String.format("%011d",group2.getCode()));
        assertEquals(OktmoLevel.Rayon, group2.getLevel());
        assertEquals("Биробиджан", group2.getName());
    }

    @Test
    public void numberOfLevel() {
        OktmoGroup group1= data.getCommonMap().get(99630000000L);
        assertEquals("Смидовичский муниципальный район", group1.getName());
        assertEquals(6, group1.getChildGroups().size());
        System.out.println(group1.getChildGroups());
        assertEquals("Николаевское",group1.getChildGroups().get(2).getName());
    }

    @Test
    public void testFindPlacesInGroup() {
        OktmoGroup group=data.getCommonMap().get(7721000000L);
        System.out.println(group.getCode()+" "+group.getLevel()+" "+group.getName());
        OktmoAnalyzer analyzer=new OktmoAnalyzer(data);
        List<Place> list=analyzer.findAllPlacesInGroup(group);
        assertEquals(52,list.size());
    }

    @Test
    public void testPopularPlaceName() {
        OktmoAnalyzer analyzer=new OktmoAnalyzer(data);
        System.out.println(analyzer.findMostPopularPlaceName("Муниципальные образования Алтайского края"));
    }

    @Test
    public void testprintStatusTable() {
        OktmoAnalyzer analyzer=new OktmoAnalyzer(data);
        System.out.println(analyzer.printStatusTableForRegion("Муниципальные образования Алтайского края"));
    }
}
