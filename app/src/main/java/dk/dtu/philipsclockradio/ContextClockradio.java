package dk.dtu.philipsclockradio;

import android.os.Handler;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ContextClockradio {
    public static MainUI ui;

    public boolean isClockRunning = false;
    private State currentState;
    private Date mTime;
    private Double mRadioChannel;
    private String mDisplayText;
    private Time alarmTime;
    private int sleepTime;

    private Handler mainHandler = new Handler();

    public ContextClockradio(MainUI context) {
        ui = context;

        //Sætter tiden til 12.00, hvis tiden ikke er sat endnu
        if (mTime == null) {
            Calendar date = Calendar.getInstance();
            date.set(2019, 1, 1, 12, 00);
            mTime = date.getTime();
        }

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

    //Opdaterer kontekst time state og UI
    void setRadio(Double radioChannel) {
        this.mRadioChannel = radioChannel;
        if (currentState.getClass().getSimpleName().equals("StateRadioOn")) {
            updateDisplayRadioChannel();
        }
    }

    void updateDisplayRadioChannel() {
        mDisplayText = mRadioChannel.toString();
        ui.setDisplayText(mDisplayText);
    }

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

    /*
    todo - slet
    void sleepRadio(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
        }
    }*/

    //Opdaterer kontekst time state og UI
    void setTime(Date time) {
        mTime = time;
        if (currentState.getClass().getSimpleName().equals("StateStandby")) {
            updateDisplayTime();
        }
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

    public void setAlarm(Time time) {
        this.alarmTime = time;

     /*   int hour = mTime.getHours();
        int minutte = mTime.getMinutes();
        LocalTime start = LocalTime.of(hour,minutte,00);

        LocalTime end = LocalTime.of(16, 59, 55);
        Duration duration = Duration.between(start, end);

        mTime
*/
    }

    public void setSleepTimer(int sleepTime) {
        this.sleepTime = sleepTime;
        System.out.println("Sleep timer on for " + sleepTime + " minutes");
        TimerRunnable timerRunnable = new TimerRunnable("sleepTimer");
        new Thread(timerRunnable).start();
    }


    //Lader fm/am blive set på displayet når der skiftes mellem disse.
    class TimerRunnable implements Runnable {
        String application = null;

        public TimerRunnable(String application) {
            this.application = application;
        }

        @Override
        public void run() {
            //todo - for hver sekund en tråd kører - giv besked om længde osv...

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
          /*  todo implement alarm - if (application.equalsIgnoreCase("alarmTime")) {
                //todo implementer

                //alarmTime.
                //toLocalTime(java.sql.Time.valueOf(LocalTime.now())))

                try {



                    SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm:ss");
                    String time = localDateFormat.format(mTime);

                    //alarmTime;
                    TimeUnit.MINUTES.sleep(secondsToSleep);
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
*/
            if (application.equalsIgnoreCase("sleepTimer")) {
                try {
                    Thread.sleep(sleepTime * 1000 * 60);
                    //todo slet - for test: Thread.sleep(8 * 1000);
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