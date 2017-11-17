package my.oktmo;

import java.util.ArrayList;
import java.util.TreeSet;

public class OktmoData {
    private ArrayList<Place> places;
    private TreeSet<String> allStatuses;
    private ArrayList<Place> sortedPlaces;
    private boolean isPlacesChanged=false;
    public OktmoData() {
        places=new ArrayList<Place>();
        allStatuses=new TreeSet<String>();
    }

    public void addPlace(Place place) {
        places.add(place);
        allStatuses.add(place.getStatus());
        isPlacesChanged=true;
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

    public TreeSet<String> getAllStatuses() {
        return allStatuses;
    }

    public Place getLastPlace() {
        return places.get(places.size()-1);
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
