package dk.dtu.philipsclockradio;

import android.os.Handler;

public class StateAlarmRunning extends StateAdapter {
    private static StateAlarmRunning instance = null;

    private Handler mainHandler = new Handler();
    private boolean muted = false;
    private int snoozeTime = 9;
    private int alarmSource;

    private StateAlarmRunning(int alarmSource) {
        this.alarmSource = alarmSource;
    }

    public static StateAlarmRunning getInstance(int alarmSetting) {
        if (instance == null) {
            instance = new StateAlarmRunning(alarmSetting);
        }
        return instance;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        if (alarmSource == 1) {
            context.updateDisplaySimpleString("ALARM");

        } else if (alarmSource == 2) {
            //context.setState(StateRadioOn.getInstance());
            StateRadioOn.getInstance().onEnterState(context);
        }else{
            System.out.println(""+alarmSource);
        }
    }

    @Override
    public void onExitState(ContextClockradio context) {
        if (muted){
            context.ui.turnOffLED(alarmSource);
            alarmSource = 0;
        }
    }

    @Override
    public void onClick_AL1(ContextClockradio context) {
        muted = true;
        context.setState(StateStandby.getInstance(context.getTime()));
    }

    @Override
    public void onClick_AL2(ContextClockradio context) {
        onClick_AL1(context);
    }

    @Override
    public void onClick_Snooze(ContextClockradio context) {
        context.setState(StateStandby.getInstance(context.getTime()));
        SnoozeRunnable runnable = new SnoozeRunnable(context);
        new Thread(runnable).start();
    }

    class SnoozeRunnable implements Runnable {
        private ContextClockradio context;

        private SnoozeRunnable(ContextClockradio context) {
            this.context = context;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000 * snoozeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!muted) {
                        context.setState(StateAlarmRunning.getInstance(alarmSource));
                        System.out.println("snap");
                    }
                }
            });
        }
    }
}
