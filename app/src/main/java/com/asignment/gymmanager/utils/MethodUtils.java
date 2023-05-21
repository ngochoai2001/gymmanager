package com.asignment.gymmanager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class MethodUtils {
    public static Date stringTodate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateTostring(Date time) {
        SimpleDateFormat mySimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return mySimpleDateFormat.format(time);
    }

    public static String getTimeNow() {
        Date date = new Date();

        String strDateFormat = "dd-MM-yyyy";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strDateFormat);
        return simpleDateFormat.format(date);
    }

    public static String UpDownDay(String time, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c1 = Calendar.getInstance();
        Date date = stringTodate(time);
        c1.setTime(date);

        c1.add(Calendar.DATE, day);

        return dateFormat.format(c1.getTime());
    }

    public static int compareDate(String date) {

        // Định nghĩa 2 mốc thời gian ban đầu
        Date date1 = stringTodate(date);
        Date date2 = stringTodate(getTimeNow());

        if (date1.equals(date2))
            return 0;//Hai ngày trùng nhau
        else if (date1.before(date2)) // Hoặc  else if (date1.after(date2)== false)
            return 1;//Trước
        else
            return 2;//Sau

    }

    public static boolean readString(String s) {
        try {
            s.charAt(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean readInt(String s) {
        try {
            s.charAt(0);
            Integer.parseInt(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean readFloat(String s) {
        try {
            s.charAt(0);
            Float.parseFloat(s);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
