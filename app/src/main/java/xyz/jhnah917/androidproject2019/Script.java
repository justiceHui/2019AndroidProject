package xyz.jhnah917.androidproject2019;


import static xyz.jhnah917.androidproject2019.Listener.send;
import java.util.Calendar;

public class Script {
    public static void main(String room, String msg, String sender){
        String prefix = MainActivity.prefix;
        msg = msg.trim();
        sender = sender.trim();
        String a[] = msg.split(" ");
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        String week[] = {"", "일", "월", "화", "수", "목", "금", "토"};
        if(msg.equals(prefix + "급식")){
            String y = String.valueOf(year);
            String m = String.valueOf(month);
            if(month < 10) m = "0" + m;
            String d = String.valueOf(date);
            if(date < 10) d = "0" + d;
            send(y + "년 " + m + "월 " + d + "일");
            send(School.food(y, m, d));
            Util.restartApp();
        }
        else if(msg.equals(prefix + "내일 급식")){
            int monthArr[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            date++;
            if(date > monthArr[month]){
                date = 1;
                month++;
            }
            if(month > 12){
                year++;
                month = 1;
            }
            String y = String.valueOf(year);
            String m = String.valueOf(month);
            if(month < 10) m = "0" + m;
            String d = String.valueOf(date);
            if(date < 10) d = "0" + d;
            send(y + "년 " + m + "월 " + d + "일");
            send(School.food(y, m, d));
            Util.restartApp();
        }
        else if(a[0].equals(prefix + "급식")){
            String y = a[1];
            String m = a[2];
            String d = a[3];
            send(School.food(y, m, d));
            Util.restartApp();
        }

        if(a[0].equals(prefix + "일정")){
            send(School.schedule(a[1], a[2], a[3]));
            Util.restartApp();
        }

        if(msg.equals(prefix + "시간표")){
            send(week[day] + "요일");
            if(day == 7 || day == 1) send("수업이 없습니다.");
            else send(School.getTimeTable(day-2));
        }else if(a[0].equals(prefix + "시간표")){
            if(a[1].equals("월")) send(School.getTimeTable(0));
            else if(a[1].equals("화")) send(School.getTimeTable(1));
            else if(a[1].equals("수")) send(School.getTimeTable(2));
            else if(a[1].equals("목")) send(School.getTimeTable(3));
            else if(a[1].equals("금")) send(School.getTimeTable(4));
            else send("입력 오류");
        }
    }
}
