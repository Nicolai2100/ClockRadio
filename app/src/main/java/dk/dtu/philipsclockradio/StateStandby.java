package dk.dtu.philipsclockradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import java.sql.Time;
import java.util.Date;

public class StateStandby extends StateAdapter {

    private static final String ALARM_SET = "alarmSet";
    private static final String ALARM_TIME = "alarmTime";
    private static final String SHARED_PREFS = "sharedPrefs";
    private static StateStandby instance = null;

    private Date mTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;
    private Date alarmTime;
    private boolean alarmSet;

    private StateStandby(Date time) {
        mTime = time;
    }

    public static StateStandby getInstance(Date time) {
        if (instance == null) {
            return instance = new StateStandby(time);
        }
        return instance;
    }

    private void loadData() {
        StateAlarmSettingMode.getInstance(this, mContext).loadData();
    }


    //Opdaterer hvert 60. sekund med + 1 min til tiden
    //samt kontrollerer om alarmen skal køres
    Runnable mSetTime = new Runnable() {

        @Override
        public void run() {
            try {
                long currentTime = mTime.getTime();
                mTime.setTime(currentTime + 60000);
                mContext.setTime(mTime);
                if (alarmSet) {
                    if (alarmTime != null && mTime.getHours() == alarmTime.getHours() && mTime.getMinutes() == alarmTime.getMinutes()) {
                        mContext.setState(StateAlarmRunning.getInstance(mContext.getAlarmSource()));
                    }
                }
            } finally {
                mHandler.postDelayed(mSetTime, 60000);
            }
        }
    };

    void startClock() {
        mSetTime.run();
        mContext.isClockRunning = true;
    }

    void stopClock() {
        mHandler.removeCallbacks(mSetTime);
        mContext.isClockRunning = false;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        //Lokal context oprettet for at Runnable kan få adgang
        mContext = context;
        context.updateDisplayTime();
        if (!context.isClockRunning) {
            startClock();
        }
        if (alarmTime == null ) {
            loadData();
        }
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        stopClock();
        context.setState(new StateSetTime());
    }

    //Tænder radioen
    @Override
    public void onClick_Power(ContextClockradio context) {
        context.setState(StateRadioOn.getInstance());
    }

    //Tænder for sleep
    @Override
    public void onClick_Sleep(ContextClockradio context) {
        context.setState(StateSleepTimerMode.getInstance());
    }


    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        context.setState(StateAlarmSettingMode.getInstance(this, mContext));
    }

    @Override
    public void onLongClick_AL2(ContextClockradio context) {
        onLongClick_AL1(context);
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        context.changeAlarmSourse();
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        onClick_AL1(context);
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
        this.alarmSet = true;
    }

    @Override
    public void onLongClick_Snooze(ContextClockradio context) {
        //todo slet dette - til test
        context.setState(StateAlarmRunning.getInstance(mContext.getAlarmSource()));
    }

    @Override
    public void onExitState(ContextClockradio context) {
    }
}
