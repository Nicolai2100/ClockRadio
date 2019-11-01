package dk.dtu.philipsclockradio;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;

import org.junit.Test;

import java.sql.Time;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.SimpleFormatter;

import static org.junit.Assert.*;

public class StateAlarmRunningSettingModeTest {
   /* MainUI mainUI = new MainUI();
    ContextClockradio context = new ContextClockradio(mainUI);
    StateAlarmSettingMode stateAlarmSettingMode = StateAlarmSettingMode.getInstance();*/

    @Test
    public void onLongClick_AL1() {
        String string1 = "20";
        String string2 = "20";
       // DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = new Date();
        date.setHours(Integer.parseInt(string1));
        date.setMinutes(Integer.parseInt(string2));
        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010

/*

        String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy, HH:mm", Locale.ENGLISH);
        date = format.parse(string);
        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
*/
        Date alarmTime;
        java.text.DateFormat sdf = new java.text.SimpleDateFormat("HH:mm");

        String string = "January 2, 2010";
        //DateFormat format = new SimpleDateFormat("HH:mm");

        alarmTime = new Time(00, 02, 0);
        String strDate = sdf.format(alarmTime);
        System.out.println(strDate);
        /*try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010*/
    }

    @Test
    public void onLongClick_AL2() {

        DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR, 12);
        today.set(Calendar.MINUTE, 00);

        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        today.setTimeZone(TimeZone.getTimeZone("UCT"));

        Date now = new Date();
        now.setTime(today.getTime().getTime());


        Calendar alarmTime = new GregorianCalendar();

        alarmTime.set(Calendar.HOUR, 18);
        alarmTime.set(Calendar.MINUTE, 00);
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MILLISECOND, 0);
        alarmTime.setTimeZone(TimeZone.getTimeZone("UCT"));

        int hour = 24 - now.getHours() + alarmTime.getTime().getHours();
       // int minutte = ;
        System.out.println(now.getHours());

        System.out.println(alarmTime.getTime().getHours());
        System.out.println(alarmTime.getTime());


//        System.out.println(dateTimeFormatter.format(now));
        System.out.println(hour);


    }
}