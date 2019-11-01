package dk.dtu.philipsclockradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import java.util.Calendar;
import java.util.Date;

public class ContextClockradio {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String ALARM_SOURCE = "alarmSource";
    public static MainUI ui;

    public boolean isClockRunning = false;
    private State currentState;
    private Date mTime;
    private Double mRadioChannel;
    private String mDisplayText;
    private Date alarmTime;
    private int sleepTime;
    private Handler mainHandler = new Handler();
    private int alarmSource;

    private void saveDate() {
        SharedPreferences sharedPreferences = MainUI.getContextOfApp().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ALARM_SOURCE, alarmSource);
        editor.apply();
    }


    public void loadData() {
        SharedPreferences sharedPreferences = MainUI.getContextOfApp().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        alarmSource = sharedPreferences.getInt(ALARM_SOURCE, 0);
        if (alarmSource > 0){
            ui.turnOnLED(alarmSource);
        }
    }

    public ContextClockradio(MainUI context) {
        ui = context;

        //Sætter tiden til 12.00, hvis tiden ikke er sat endnu
        if (mTime == null) {
            Calendar date = Calendar.getInstance();
            date.set(2019, 1, 1, 12, 00);
            mTime = date.getTime();
        }

        loadData();

        //Når app'en starter, så går vi ind i Standby State
        currentState = StateStandby.getInstance(mTime);
        currentState.onEnterState(this);
    }

    //setState er når vi skifter State
    void setState(final State newState) {
        currentState.onExitState(this);
        currentState = newState;
        currentState.onEnterState(this);
        System.out.println("Current state: " + newState.getClass().getSimpleName());
    }

    void updateDisplayTime() {
        mDisplayText = mTime.toString().substring(11, 16);
        ui.setDisplayText(mDisplayText);
    }

    //Opdaterer kontekst radioChannel state og UI
    void setRadio(Double radioChannel) {
        this.mRadioChannel = radioChannel;
        if (currentState.getClass().getSimpleName().equals("StateRadioOn")) {
            updateDisplayRadioChannel();
        }
    }

    //Opdaterer UI
    void updateDisplayRadioChannel() {
        mDisplayText = mRadioChannel.toString();
        ui.setDisplayText(mDisplayText);
    }

    //Opdaterer UI
    void updateDisplaySimpleString(String input) {
        ui.setDisplayText(input);
    }

    void updateDisplayRadioFrequency(String frequency) {
        System.out.println("Current frequency: " + frequency);
        ui.setDisplayText(frequency);

        TimerRunnable timerRunnable = new TimerRunnable("frequency");
        new Thread(timerRunnable).start();
    }

    public Date getTime() {
        return mTime;
    }

    //Opdaterer kontekst time state og UI
    void setTime(Date time) {
        mTime = time;
        if (currentState.getClass().getSimpleName().equals("StateStandby")) {
            updateDisplayTime();
        }
    }

    public void setAlarm(Date time) {
        this.alarmTime = time;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setSleepTimer(int sleepTime) {
        this.sleepTime = sleepTime;
        System.out.println("Sleep timer on for " + sleepTime + " minutes");
        TimerRunnable timerRunnable = new TimerRunnable("sleepTimer");
        new Thread(timerRunnable).start();
    }

    public int getAlarmSource() {
        return alarmSource;
    }

    public void setAlarmSource(int alarmSource) {
        this.alarmSource = alarmSource;
    }

    public void changeAlarmSourse() {
        ++alarmSource;
        if (alarmSource > 3) {
            alarmSource = 1;
        }
        if (alarmSource == 1) {
            ui.turnOnLED(1);
        } else if (alarmSource == 2) {
            ui.turnOffLED(1);
            ui.turnOnLED(2);
        } else if (alarmSource == 3) {
            ui.turnOffLED(2);
        }
        saveDate();
    }

    //Disse metoder bliver kaldt fra UI tråden
    public void onClick_Hour() {
        currentState.onClick_Hour(this);
    }

    public void onClick_Min() {
        currentState.onClick_Min(this);
    }

    public void onClick_Preset() {
        currentState.onClick_Preset(this);
    }

    public void onClick_Power() {
        currentState.onClick_Power(this);
    }

    public void onClick_Sleep() {
        currentState.onClick_Sleep(this);
    }

    public void onClick_AL1() {
        currentState.onClick_AL1(this);
    }

    public void onClick_AL2() {
        currentState.onClick_AL2(this);
    }

    public void onClick_Snooze() {
        currentState.onClick_Snooze(this);
    }

    public void onLongClick_Hour() {
        currentState.onLongClick_Hour(this);
    }

    public void onLongClick_Min() {
        currentState.onLongClick_Min(this);
    }

    public void onLongClick_Preset() {
        currentState.onLongClick_Preset(this);
    }

    public void onLongClick_Power() {
        currentState.onLongClick_Power(this);
    }

    public void onLongClick_Sleep() {
        currentState.onLongClick_Sleep(this);
    }

    public void onLongClick_AL1() {
        currentState.onLongClick_AL1(this);
    }

    public void onLongClick_AL2() {
        currentState.onLongClick_AL2(this);
    }

    public void onLongClick_Snooze() {
        currentState.onLongClick_Snooze(this);
    }

    class TimerRunnable implements Runnable {
        String application = null;

        public TimerRunnable(String application) {
            this.application = application;
        }

        @Override
        public void run() {
            //Lader fm/am blive set på displayet når der skiftes mellem disse.
            if (application.equalsIgnoreCase("frequency")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        updateDisplayRadioChannel();
                    }
                });
            }
            //Hvis sleep-funktionen er aktiveret går den fra radio til standby efter tiden er gået
            if (application.equalsIgnoreCase("sleepTimer")) {
                try {
                    Thread.sleep(sleepTime * 1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ui.turnOffLED(3);
                        if (currentState == StateRadioOn.getInstance()) {
                            setState(StateStandby.getInstance(getTime()));
                        }
                    }
                });
            }
        }
    }
}