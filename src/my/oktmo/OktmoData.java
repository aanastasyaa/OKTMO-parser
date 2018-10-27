package my.oktmo;

import java.util.*;

/**
 * Представляет собой хранилище для населенных пунктов, муниципальных образований,
 * статусов НП, полученных при чтении файла с классификацией ОКТМО.
 * Содержит методы доступа к приватным полям класса.
 */
public class OktmoData {

    private ArrayList<Place> places;
    private Set<String> allStatuses;
    private ArrayList<Place> sortedPlaces;

    //определяет необходимость проведения сортировки sortedPlaces в методе getSortedPlaces
    //принимает значение true, когда в список places добавили новый НП
    private boolean isPlacesChanged=false;

    private TreeMap<Long, OktmoGroup> commonMapGroups;
    private TreeMap<Long, Place> placesMap;
    private TreeMap<String, OktmoGroup> groupsKeyString;

    public OktmoData() {
        places=new ArrayList<Place>();
        allStatuses=new TreeSet<String>();
        commonMapGroups=new TreeMap<Long, OktmoGroup>();
        placesMap=new TreeMap<Long, Place>();
        groupsKeyString=new TreeMap<String, OktmoGroup>();
    }

    public void addPlace(Place place) {
        places.add(place);
        allStatuses.add(place.getStatus());
        placesMap.put(place.getCode(), place);
        isPlacesChanged=true;
    }

    public void addGroup(OktmoGroup group) {
        commonMapGroups.put(group.getCode(), group);
        groupsKeyString.put(group.getName(),group);
    }

    public ArrayList<Place> getSortedPlaces() {
        if (sortedPlaces==null || isPlacesChanged) {
            sortedPlaces=new ArrayList<Place>(places);
            sortedPlaces.sort(new PlaceNameComparator());
        }
        return sortedPlaces;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public Set<String> getAllStatuses() {
        return allStatuses;
    }

    public Place getLastPlace() {
        return places.get(places.size()-1);
    }

    public TreeMap<Long, Place> getPlacesMap() {
        return placesMap;
    }

    public TreeMap<String, OktmoGroup> getGroupsKeyString() {
        return groupsKeyString;
    }

    public TreeMap<Long, OktmoGroup> getCommonMap() {
        return commonMapGroups;
    }

    @Override
    public String toString() {
        return places.toString();
    }
}
