package my.oktmo;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Класс для просмотра и поиска населенных пунктов и муниципальных образований
 * в OktmoData.
 * Экземпляр класса инициализируется конструктором, принимающим объект OktmoData
 */
public class OktmoAnalyzer {//анализ объектов OktmoData
    private OktmoData data;

    public OktmoAnalyzer(OktmoData data) {
        this.data=data;
    }

    //регулярное выражение для поиска объектов Place,
    //название которых содержит меньше 6 букв и заканчиваются на -ово
    private Pattern regexpOvo = Pattern.compile("^[А-Яа-я]{1,2}ово$");

    public ArrayList<Place> findPlacesLess6symbolsAndOvo() {
        return checkPlaces(regexpOvo);
    }

    //регулярное выражение для поиска объектов Place с названиями,
    //которые начинаются и заканчиваются на одну и ту же согласную букву
    private Pattern regexpSameConsonant = Pattern.compile("^([^аеёиоуэюя]).+(\\1)$",
            Pattern.CASE_INSENSITIVE+Pattern.UNICODE_CASE);

    public ArrayList<Place> findSameConsonant() {
        return checkPlaces(regexpSameConsonant);
    }

    /**
     * Поиск объектов Place, название которых удовлетворяет регулярному выражению
     * @param regexp    регулярное выражение, критерий поиска
     * @return          список найденных <code>Place</code>, соответствующих регулярному выражению
     */
    private ArrayList<Place> checkPlaces(Pattern regexp) {
        ArrayList<Place> finding=new ArrayList<Place>();
        for (Place place: data.getPlaces() ) {
            if(regexp.matcher(place.getName()).matches())//сопоставление
                finding.add(place);
        }
        return finding;
    }

    /**
     * Метод, возвращающий List населенных пунктов Place,
     * которые относятся к данному OktmoGroup.
     * @param group     объект <code>OktmoGroup</code>, по которому ведется поиск
     * @return          список <code>Place</code> - НП, относящихся к group
     */
    public List<Place> findAllPlacesInGroup(OktmoGroup group) {
        Long startCode=group.getCode();
        Long endCode=0L;
        if(group.getLevel()==OktmoLevel.Region)
            endCode=startCode+1000000000L;
        if(group.getLevel()==OktmoLevel.Rayon)
            endCode=startCode+1000000L;
        if(group.getLevel()==OktmoLevel.Poselenie)
            endCode=startCode+1000L;
        List<Place> result=new ArrayList<Place>(data.getPlacesMap().subMap(startCode, endCode).values());
        return result;
    }

    /**
     * Находит самое популярное название населенного пункта в регионе
     * @param regionName    название региона
     * @return              самое популярное название НП или <code>null</code>, если регион с таким названием не найден
     */
    public String findMostPopularPlaceName(String regionName) {
        if(!data.getGroupsKeyString().containsKey(regionName))
            return null;
        List<Place> groupPlaces=findAllPlacesInGroup(data.getGroupsKeyString().get(regionName));
        TreeMap<String, Long> map=groupPlaces
                .stream()
                .collect(Collectors.groupingBy(Place::getName, TreeMap::new, Collectors.counting())
        );

        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    /**
     * Метод подсчитывает количество населенных пунктов с каждым статусом в регионе {@code regionName}
     * и возвращает {@code Map<String, Long>}, в котором каждому статусу соответствует число - количество НП с таким статусом
     * @param regionName    название региона
     * @return              {@code Map<String, Long>}, где String - статус и Long - количество НП с таким статусом
     */
    public Map<String, Long> printStatusTableForRegion(String regionName) {
        if(!data.getGroupsKeyString().containsKey(regionName))
            return null;
        List<Place> groupPlaces=findAllPlacesInGroup(data.getGroupsKeyString().get(regionName));
        TreeMap<String, Long> map=groupPlaces
                .stream()
                .collect(Collectors.groupingBy(Place::getStatus,TreeMap::new, Collectors.counting())
        );
        return map;
    }
}
