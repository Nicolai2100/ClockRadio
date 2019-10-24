package dk.dtu.philipsclockradio;

import android.content.SharedPreferences;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StateAlarmSettingMode extends StateAdapter {
    private static StateAlarmSettingMode instance = null;

    private SharedPreferences sharedPreferences;
    private boolean alarmOn;
    private int hour = 00;
    private int minutte = 00;
    Date alarmTime;
    DateFormat sdf = new SimpleDateFormat("HH:mm");
    StateStandby stateStandby;

    private StateAlarmSettingMode(StateStandby stateStandby) {
        this.stateStandby = stateStandby;
        alarmTime = new Time(00, 00, 0);
    }

    public static StateAlarmSettingMode getInstance(StateStandby stateStandby) {
        if (instance == null) {
            instance = new StateAlarmSettingMode(stateStandby);
        }
        return instance;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnTextBlink();
        String strDate = sdf.format(alarmTime);
        context.updateDisplaySimpleString(strDate);
        //sharedPreferences = new Sha
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
        alarmTime.setHours(12);
        alarmTime.setMinutes(02);
        context.setAlarm(alarmTime);
        if (context.getAlarmSource() == 0) {
            context.setAlarmSource(1);
            context.ui.turnOnLED(1);

        }
        stateStandby.setAlarmTime(alarmTime);
        System.out.println("Alarm set for " + alarmTime);
        context.setState(StateStandby.getInstance(context.getTime()));
    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        onLongClick_AL1(context);
    }
}
