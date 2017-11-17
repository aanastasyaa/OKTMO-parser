package my.oktmo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class OktmoReader {//считывает .txt

    private boolean checkIsIncludeUseName(String name) {//true, если прошел проверку
        String[] start= new String[] { "Городские поселения", "Городские округа",
                "Межселенные территории", "Внутригородские муниципальные образования", "Муниципальные образования",
                "Сельские поселения", "Муниципальные районы", "Муниципальные пункты"
        };
        for(int i=0; i<start.length; ++i)
            if(name.startsWith(start[i]))
                return false;
        return true;
    }

    public void readPlaces(String fileName, OktmoData data) {
        int line=0;
        try(BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "cp1251"))) {
            String s;
            while ((s=reader.readLine())!=null) {
                line++;
                String[] substrs=s.split(";");


                for (int i = 0; i < 7; i++) {//избавляемся от лишних кавычек
                    int len=substrs[i].length();
                    substrs[i]=substrs[i].substring(1,len-1);
                }
                //System.out.println(Arrays.toString(substrs));
                if(!checkIsIncludeUseName(substrs[6]))
                    continue;
                if(substrs[1].equals("000"))
                    continue;
                if(substrs[6].startsWith("Населенные пункты, входящие")) {//определяем status предыдущей строки
                    if (substrs[6].contains("городского округа"))
                        data.getLastPlace().setStatus("городской округ");
                    else if (substrs[6].contains("городского поселения"))
                        data.getLastPlace().setStatus("городское поселение");
                    else data.getLastPlace().setStatus("сельское поселение");
                    continue;
                }

                Place place = parsePlace(substrs);
                //System.out.println(line+" "+place.getStatus()+" "+place.getName());
                data.addPlace(place);
            }

        }
        catch(IOException e) {
            System.out.println("Reading error in line " + line);
            e.printStackTrace();
        }
        //System.out.println(data.toString());
    }


    private Place parsePlace(String[] substrs) {
        String code=substrs[0]+substrs[1]+substrs[2]+substrs[3];
        String name=substrs[6];
        String status;
        if(name.endsWith("сельсовет"))
            status="cельское поселение";
        else if(name.endsWith("муниципальный район"))
            status="муниципальный район";
        else if(name.endsWith("наслег"))
            status="наслег";
        else {
            //50859, 50861
            int i;
            for (i=0; i<name.length(); ++i) {//50568 "п ж/д ст"
                if(name.charAt(i)>='А' && name.charAt(i)<='Я' || name.charAt(i)=='Ё' || name.charAt(i)>='0' && name.charAt(i)<='9')
                    break;
            }
            if(i>0) {
                status = name.substring(0, i - 1);
                name = name.substring(i);
            }
            else status="";
        }
        return new Place(Long.parseLong(code), status, name);
    }
}
