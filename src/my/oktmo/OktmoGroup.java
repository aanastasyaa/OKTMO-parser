package my.oktmo;

import java.util.ArrayList;

/**
 * Представляет собой муниципальное образование, которое может быть
 * регионом, районом или поселением, на это указывает свойство {@code level}.
 * Имеет также код типа Long, название и список вложенных {@code OktmoGroup}, принадлежащих данному муниципальному образованию
 */
public class OktmoGroup {
    private OktmoLevel level;
    private String name;
    private Long code;
    private ArrayList<OktmoGroup> childGroups;

    public OktmoGroup(OktmoLevel level, Long code, String name) {
        this.level=level;
        this.code=code;
        this.name=name;
        childGroups=new ArrayList<OktmoGroup>();
    }

    public Long getCode() {
        return code;
    }

    public OktmoLevel getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public ArrayList<OktmoGroup> getChildGroups() {
        return childGroups;
    }

    public void addChildGroup(OktmoGroup group) {
        childGroups.add(group);
    }

    @Override
    public String toString() {
        String escapes="";
        if(level==OktmoLevel.Rayon)
            escapes="   ";
        else if(level==OktmoLevel.Poselenie)
            escapes="      ";
        return escapes+String.format("%011d",code)+" "+level+" "+name+"\n";
    }
}
