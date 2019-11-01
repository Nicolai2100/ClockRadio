package dk.dtu.philipsclockradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StateAlarmSettingMode extends StateAdapter {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String ALARM_SET = "alarmSet";
    private static final String ALARM_TIME = "alarmTime";
    private static StateAlarmSettingMode instance = null;

    private boolean alarmOn;
    private int hour = 00;
    private int minutte = 00;
    private Date alarmTime;
    private DateFormat sdf = new SimpleDateFormat("HH:mm");
    private StateStandby stateStandby;
    private ContextClockradio context;

    private void saveDate() {
        if (alarmTime != null) {
            SharedPreferences sharedPreferences = MainUI.getContextOfApp().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(ALARM_SET, alarmOn);
            editor.putString(ALARM_TIME, alarmTime.toString());
            editor.apply();
            Toast.makeText(MainUI.getContextOfApp(), "Data saved: " + alarmTime.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void loadData() {
        SharedPreferences sharedPreferences = MainUI.getContextOfApp().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        alarmOn = sharedPreferences.getBoolean(ALARM_SET, false);
        alarmTime = Time.valueOf(sharedPreferences.getString(ALARM_TIME, "00.00"));
        Toast.makeText(MainUI.getContextOfApp(), "Loaded" + alarmTime.toString(), Toast.LENGTH_LONG).show();

        if (alarmOn){
            onLongClick_AL1(context);
        }
    }


    private StateAlarmSettingMode(StateStandby stateStandby, ContextClockradio context) {
        this.stateStandby = stateStandby;
        this.context = context;
        alarmTime = new Time(00, 00, 0);

    }


    public static StateAlarmSettingMode getInstance(StateStandby stateStandby, ContextClockradio context) {
        if (instance == null) {
            instance = new StateAlarmSettingMode(stateStandby, context);
        }
        return instance;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        hour = alarmTime.getHours();
        minutte = alarmTime.getMinutes();
        context.ui.turnOnTextBlink();
        String strDate = sdf.format(alarmTime);
        context.updateDisplaySimpleString(strDate);
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
        saveDate();
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
      /*  alarmTime.setHours(hour);
        alarmTime.setMinutes(minutte);*/
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
