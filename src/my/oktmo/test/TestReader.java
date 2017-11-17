package my.oktmo.test;

import my.oktmo.OktmoAnalyzer;
import my.oktmo.OktmoData;
import my.oktmo.OktmoReader;
import my.oktmo.Place;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class TestReader {
    OktmoData data;

    @Before
    public void before() {
        data=new OktmoData();
        OktmoReader reader=new OktmoReader();
        reader.readPlaces("data-201710.csv",data);
    }

    @Test
    public void testData() {
        Place place1= data.getPlaces().get(0);
        Place place2=data.getLastPlace();
        assertEquals("01601000000", String.format("%011d",place1.getCode()));
        assertEquals("муниципальный район", place1.getStatus());
        assertEquals("Алейский муниципальный район", place1.getName());
        assertEquals("99701000001", String.format("%011d",place2.getCode()));
        assertEquals("г",place2.getStatus());
        assertEquals("Биробиджан", place2.getName());
    }

    @Test
    public void testRegularExpOvo() {
        OktmoAnalyzer analyzer=new OktmoAnalyzer();
        ArrayList<Place> places=analyzer.findPlacesLess6symbolsAndOvo(data);
        System.out.println(places.toString());
        assertEquals("Яново", places.get(0).getName());

        assertEquals("Елово", places.get(places.size()-1).getName());
    }

    @Test
    public void testRegularSameConsonant() {
        OktmoAnalyzer analyzer=new OktmoAnalyzer();
        ArrayList<Place> places=analyzer.findSameConsonant(data);
        System.out.println(places.toString());
    }
}
