package my.oktmo;

import java.util.*;

public class OktmoData {
    private ArrayList<Place> places;
    private Set<String> allStatuses;
    private ArrayList<Place> sortedPlaces;
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
        /*String res="";
        for (Place place: places ) {
            res+=place.toString()+"\n";
        }
        return res;*/
        return places.toString();
    }
}
