package my.oktmo;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class OktmoAnalyzer {//анализ объектов OktmoData
    public OktmoAnalyzer() {}

    public ArrayList<Place> findPlacesLess6symbolsAndOvo(OktmoData data) {
        Pattern regexp = Pattern.compile("^[А-Яа-я]{1,2}ово$");
        return checkPlaces(data, regexp);
    }

    public ArrayList<Place> findSameConsonant(OktmoData data) {
        Pattern regexp = Pattern.compile("^([^аеёиоуэюя]).+(\\1)$",Pattern.CASE_INSENSITIVE+Pattern.UNICODE_CASE);
        return checkPlaces(data, regexp);
    }

    private ArrayList<Place> checkPlaces(OktmoData data, Pattern regexp) {
        ArrayList<Place> finding=new ArrayList<Place>();
        for (Place place: data.getPlaces() ) {
            if(regexp.matcher(place.getName()).matches())//сопоставление
                finding.add(place);
        }
        return finding;
    }
}
