
package my.oktmo;

public class OktmoMain {

 public static void main(String[] args) {

     OktmoData data=new OktmoData();
     OktmoReader reader=new OktmoReader();
     reader.readPlacesAndGroups("data-201710.csv",data);
  
     System.out.println(data.getSortedPlaces().toString());
     System.out.println(data.getAllStatuses().toString());
     System.out.println(data.getCommonMap().toString());
 }
 
}
