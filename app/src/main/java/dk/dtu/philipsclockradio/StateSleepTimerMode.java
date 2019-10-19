package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateSleepTimerMode extends StateAdapter {

    private static StateSleepTimerMode instant = null;
    private Handler mainHandler = new Handler();
    private int sleepTime;
    private boolean sleepTimerOn;
    private ContextClockradio context;

    private StateSleepTimerMode() {
    }

    public static StateSleepTimerMode getInstant() {
        if (instant == null) {
            instant = new StateSleepTimerMode();
        }
        return instant;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        this.context = context;
        sleepTime = 120;
        sleepTimerOn = true;
        setSleepTimerLed();
        context.updateDisplaySimpleString(120 + "");
        idleMethod();
    }

    @Override
    public void onClick_Sleep(ContextClockradio context) {
        if (sleepTime > 30) {
            sleepTime = sleepTime - 30;
        } else if (sleepTime == 30) {
            sleepTime = 15;
        } else if (sleepTime == 15) {
            sleepTime = 0;
            sleepTimerOn = false;
        } else if (sleepTime == 0) {
            sleepTime = 120;
            sleepTimerOn = true;
        }
        setSleepTimerLed();
        context.updateDisplaySimpleString(sleepTime + "");
        idleMethod();
    }

    void setSleepTimerLed() {
        if (sleepTimerOn) {
            context.ui.turnOnLED(3);
        } else
            context.ui.turnOffLED(3);
    }

    void idleMethod() {
        CountDownRunnable runnable = new CountDownRunnable(5);
        new Thread(runnable).start();
    }

    void back() {
        context.setState(StateStandby.getInstance(context.getTime()));
    }

    class CountDownRunnable implements Runnable {
        int seconds;

        CountDownRunnable(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            for (int i = 0; i < seconds; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    back();
                }
            });
        }
    }
}
