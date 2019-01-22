package my.oktmo;

import java.util.Comparator;

/**
 * Представляет собой населенный пункт согласно классификации ОКТМО.
 * Каждый населенный пункт имеет свой код из 11 цифр,
 * название и статус.
 * Реализован {@code Comparator} для упорядочивания по {@code name}
 */
public class Place /*implements Comparable<Place>*/ {
  private long code;
  private String status;
  private String name;

  public Place(long code, String status, String name) {
    this.code=code;
    this.name=name;
    this.status=status;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public long getCode() {
    return code;
  }

  @Override
  public String toString() {
    return String.format("%011d",code)+" "+status+" "+name+"\n";
  }

  /*@Override
  public int compareTo(Place o) {
    return name.compareTo(o.name);
  }*/
}

class PlaceNameComparator implements Comparator<Place> {
  @Override
  public int compare(Place o1, Place o2) {
    return o1.getName().compareTo(o2.getName());
  }
}
