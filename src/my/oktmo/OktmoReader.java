package my.oktmo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class OktmoReader {//считывает .txt
    private static final String[] start= new String[] { "Городские поселения", "Городские округа",
            "Межселенные территории", "Внутригородские муниципальные образования", "Населенные пункты, входящие в ",
            "Сельские поселения", "Муниципальные районы", "Муниципальные пункты"
    };
    private boolean checkIsIncludeUseName(String name) {//true, если прошел проверку

        for(int i=0; i<start.length; ++i)
            if(name.startsWith(start[i]))
                return false;
        return true;
    }

    private Pattern forLine=Pattern.compile(//кавычки есть
            "^\"(\\d{2})\";"+
                    "\"(\\d{3})\";"+
                    "\"(\\d{3})\";"+
                    "\"(\\d{3})\";"+
                    "\"(\\d)\";"+
                    "\"(\\d)\";"+
                    "\"((?:[а-я|/|-]*\\s)*)"+//статус //?: для того, чтобы внутренняя группа не сохранялась (для ст ж/д это ж/д)
                    //"((?:[А-ЯЁ№][а-я0-9|/|-]+)(?:[а-я0-9|/|-|\\s]*))\""//название
                    //"\"(.*)\";"+"\"(.*)\";"+"\"(.*)\";"+"(.*);"+"(.*);"+"(.*);"
                    "((?:[А-ЯЁ№][А-Яа-яЁё0-9/\\-\\s]+))\";"
    );

    private OktmoGroup lastRegion=null;
    private OktmoGroup lastRayon=null;
    private OktmoGroup lastPoselenie=null;

    public void readPlacesAndGroups(String fileName, OktmoData data) {
        try(Stream<String> lines= Files.lines(Paths.get(fileName), Charset.forName("cp1251"))) {
            //применяем регулярное выражение и фильтруем записи с помощью checkIsIncludeUseName
            Stream<Matcher> filteredLines=lines.map(line -> forLine.matcher(line))
                                                    .filter(line -> line.find())
                                                    .filter(matcher -> checkIsIncludeUseName(matcher.group(8)));


            filteredLines.forEach( line-> {
                    long code=Long.parseLong(line.group(1)+line.group(2)+line.group(3)+line.group(4));
                    String name = line.group(8);
                    String status=line.group(7).trim();
                    /*if (line.group(2).equals("000")) {//регион
                        lastRegion=new OktmoGroup(OktmoLevel.Region, code, name);
                        data.addGroup(lastRegion);
                    }
                    else if(line.group(3).equals("000")) {//район
                        lastRayon=new OktmoGroup(OktmoLevel.Rayon,code, name);
                        lastRegion.addChildGroup(lastRayon);
                        data.addGroup(lastRayon);
                    }
                    else if(line.group(4).equals("000")) {//поселение
                        lastPoselenie=new OktmoGroup(OktmoLevel.Poselenie, code, name);
                        lastRayon.addChildGroup(lastPoselenie);
                        data.addGroup(lastPoselenie);
                    }
                    else {//насел пункт
                        data.addPlace(new Place(code,status,name));
                    }*/

                    if(!line.group(4).equals("000")) {
                        data.addPlace(new Place(code, status, name));
                    } else {
                        if(line.group(3).equals("000")) {
                            if(line.group(2).equals("000")) {//регион
                                lastRegion=new OktmoGroup(OktmoLevel.Region, code, name);
                                data.addGroup(lastRegion);
                            }
                            //***.000.000
                            else {//район
                                lastRayon=new OktmoGroup(OktmoLevel.Rayon,code, name);
                                lastRegion.addChildGroup(lastRayon);
                                data.addGroup(lastRayon);
                            }
                        }
                        //***.***.000
                        else {//поселение
                            lastPoselenie=new OktmoGroup(OktmoLevel.Poselenie, code, name);
                            lastRayon.addChildGroup(lastPoselenie);
                            data.addGroup(lastPoselenie);
                        }
                    }
            });
        }
        catch(IOException e) {
            System.out.println("Reading error");
            e.printStackTrace();
        }

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
        if(name.endsWith("муниципальный район"))
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
