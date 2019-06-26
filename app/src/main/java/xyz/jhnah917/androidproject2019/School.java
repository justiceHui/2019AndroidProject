package xyz.jhnah917.androidproject2019;


import java.lang.Thread;

import static java.lang.Integer.parseInt;

public class School {


    public static String food(String y, String m, String d) {
        if(y.length() != 4 || m.length() != 2 || d.length() != 2) return "입력 형식은 YYYY MM DD입니다.";
        try {
            int tmp = parseInt(d);
            d = String.valueOf(tmp);
            final String split1 = "<td><div>" + d + "<br />";
            final String split2 = "</div></td>";
            //Listener.send(split1 + "\n" + split2);
            String url = "https://stu.sen.go.kr/sts_sci_md00_001.do?schulCode=B100000658&schulCrseScCode=4&schulKndScCode=04&ay=" + y + "&mm=" + m + "&";
            //Listener.send(url);
            String code = Util.WWW(url);
            String foodStr = code.split(split1)[1];
            //Listener.send(foodStr + "\n11111111111111111");
            foodStr = foodStr.split(split2)[0];
            //Listener.send(foodStr + "\n22222222222222222");
            foodStr = foodStr.replace("<br />", "\n").replace("[석식]", "\n[석식]");
            return (foodStr + "\n\n* 알레르기 정보\n1.난류 2.우유 3.메밀 4.땅콩 5.대두 6.밀 7.고등어 8.게 9.새우 10.돼지고기 11.복숭아 12.토마토 13.아황산류 14.호두 15.닭고기16.쇠고기 17.오징어 18.조개류(굴,전복,홍합 포함)");
        }catch(Exception e){
            return "급식 정보가 없습니다." + "\n\n" + e.toString();
        }
    }

    public static String schedule(String y, String m, String d){
        if(y.length() != 4 || m.length() != 2 || d.length() != 2) return "입력 형식은 YYYY MM DD입니다.";
        int[] dayOfMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int yy = parseInt(y);
        if (yy % 400 == 0 || (yy % 4 == 0 && yy % 100 != 0)) dayOfMonth[2] = 29;
        try{
            String nextDay;
            int tmp = parseInt(d)+1;
            if(tmp  < 10) nextDay = "0" + String.valueOf(tmp);
            else nextDay = String.valueOf(tmp);
            final String daySplit = "<em>" + d + "</em>";
            String nextSplit = "<em>" + nextDay + "</em>";
            if (parseInt(nextDay) > dayOfMonth[parseInt(m)]) nextSplit = "</div>";
            String url = "https://stu.sen.go.kr/sts_sci_sf01_001.do?schulCode=B100000658&schulCrseScCode=4&schulKndScCode=04&ay=" + y + "&mm=" + m;
            String fullHtml = Util.WWW(url);
            String daySchedule = fullHtml.split(daySplit)[1].split(nextSplit)[0];
            String[] dayScheduleSplit1 = daySchedule.split("<strong>");
            String ret = "";
            for(int i=1; i<dayScheduleSplit1.length; i++){
                ret += dayScheduleSplit1[i].split("</strong>")[0] + "\n";
            }
            if(ret.trim().equals("")) return "일정 정보가 없습니다.";
            return ret;
        }catch(Exception e){
            return "일정 정보가 없습니다." + "\n\n" + e.toString();
        }
    }

    public static String getTimeTable(int x){
        String[] table = {"겜프\n겜프\n과학A\n수학\n앱프\n앱프\n한국사",
                "과학B\n정통\n영어A\n한국사\n일본어\n수학\n국어",
                "앱프\n앱프\n진로\n국어\n겜프\n겜프\n동아리",
                "영어A\n한국사\n일본어\n영어B\n정통\n정통\n국어",
                "체육\n일본어\n수학\n과학A\n창체"
        };
        return table[x];
    }


}
