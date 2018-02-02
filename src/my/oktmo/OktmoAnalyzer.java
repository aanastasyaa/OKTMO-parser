package my.oktmo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OktmoAnalyzer {//анализ объектов OktmoData
    private OktmoData data;

    public OktmoAnalyzer(OktmoData data) {
        this.data=data;
    }

    private Pattern regexpOvo = Pattern.compile("^[А-Яа-я]{1,2}ово$");

    public ArrayList<Place> findPlacesLess6symbolsAndOvo() {
        return checkPlaces(regexpOvo);
    }

    private Pattern regexp = Pattern.compile("^([^аеёиоуэюя]).+(\\1)$",Pattern.CASE_INSENSITIVE+Pattern.UNICODE_CASE);

    public ArrayList<Place> findSameConsonant() {
        return checkPlaces(regexp);
    }

    private ArrayList<Place> checkPlaces(Pattern regexp) {
        ArrayList<Place> finding=new ArrayList<Place>();
        for (Place place: data.getPlaces() ) {
            if(regexp.matcher(place.getName()).matches())//сопоставление
                finding.add(place);
        }
        return finding;
    }

    public ArrayList<Place> findAllPlacesInGroup(OktmoGroup group) {
        Long startCode=group.getCode();
        Long endCode=0L;
        if(group.getLevel()==OktmoLevel.Region)
            endCode=startCode+1000000000L;
        if(group.getLevel()==OktmoLevel.Rayon)
            endCode=startCode+1000000L;
        if(group.getLevel()==OktmoLevel.Poselenie)
            endCode=startCode+1000L;
        ArrayList<Place> result=new ArrayList<Place>(data.getPlacesMap().subMap(startCode, endCode).values());
        return result;
    }

    class PopularNamesComparator implements Comparator<Map.Entry<String,Long>> {
        @Override
        public int compare(Map.Entry<String,Long> o1, Map.Entry<String,Long> o2) {
            int l;
            if(Long.compare(o1.getValue(), o2.getValue())==0)
                l=o1.getKey().compareTo(o2.getKey());
            else l=o1.getValue().compareTo(o2.getValue());
            return l;
        }
    }

    public String findMostPopularPlaceName(String regionName) {
        if(!data.getGroupsKeyString().containsKey(regionName))
            return null;
        ArrayList<Place> groupPlaces=findAllPlacesInGroup(data.getGroupsKeyString().get(regionName));
        TreeMap<String, Long> map=groupPlaces
                .stream()
                .collect(Collectors.groupingBy(Place::getName, TreeMap::new, Collectors.counting())
        );
        //Map<String, Long> map=groupPlaces.stream().collect(Collectors.toMap(Place::getName, Collectors.counting()));
        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    public Map<String, Long> printStatusTableForRegion(String regionName) {
        if(!data.getGroupsKeyString().containsKey(regionName))
            return null;
        ArrayList<Place> groupPlaces=findAllPlacesInGroup(data.getGroupsKeyString().get(regionName));
        TreeMap<String, Long> map=groupPlaces
                .stream()
                .collect(Collectors.groupingBy(Place::getStatus,TreeMap::new, Collectors.counting())
        );
        return map;
    }
}
