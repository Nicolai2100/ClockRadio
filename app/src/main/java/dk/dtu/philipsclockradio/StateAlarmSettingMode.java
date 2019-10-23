package dk.dtu.philipsclockradio;

import java.io.InputStreamReader;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentNavigableMap;

public class StateAlarmSettingMode extends StateAdapter {
    private static StateAlarmSettingMode instance = null;
    private boolean alarmOn;
    private int hour = 00;
    private int minutte = 00;
    Time alarmTime;
    DateFormat sdf = new SimpleDateFormat("HH:mm");

    private StateAlarmSettingMode() {
        alarmTime = new Time(00, 00, 0);
    }

    public static StateAlarmSettingMode getInstance() {
        if (instance == null) {
            instance = new StateAlarmSettingMode();
        }
        return instance;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnTextBlink();
        String strDate = sdf.format(alarmTime);
        context.updateDisplaySimpleString(strDate);
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        ++hour;
        if (hour > 23) {
            hour = 0;
        }
        alarmTime = new Time(hour, minutte, 0);
        String strDate = sdf.format(alarmTime);
        context.updateDisplaySimpleString(strDate);
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        ++minutte;
        if (minutte > 59) {
            minutte = 0;
            ++hour;
        }
        alarmTime = new Time(hour, minutte, 0);
        String strDate = sdf.format(alarmTime);
        context.updateDisplaySimpleString(strDate);
    }

    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        //alarm on
        alarmOn = true;
        context.changeAlarmSourse();
        context.setAlarm(alarmTime);



        System.out.println("Alarm set for " + alarmTime);
        context.setState(StateStandby.getInstance(context.getTime()));

        Date today = context.getTime();
        today.setHours(hour);
        today.setMinutes(minutte);

        /*
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minutte);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(TimeZone.getTimeZone("CET"));
        System.out.println(calendar.getTime());
        System.out.println(calendar);*/
    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        onLongClick_AL1(context);
    }
}
