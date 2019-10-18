package dk.dtu.philipsclockradio;

import android.os.Handler;

import java.util.Date;

public class StateStandby extends StateAdapter {

    private static StateStandby instance = null;

    private Date mTime;
    private static Handler mHandler = new Handler();
    private ContextClockradio mContext;

    private StateStandby(Date time) {
        mTime = time;
    }

    public static StateStandby getInstance(Date time) {
        if (instance == null){
            return instance = new StateStandby(time);
        }
        return instance;
    }

    //Opdaterer hvert 60. sekund med + 1 min til tiden
    Runnable mSetTime = new Runnable() {

        @Override
        public void run() {
            try {
                long currentTime = mTime.getTime();
                mTime.setTime(currentTime + 60000);
                mContext.setTime(mTime);
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
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        stopClock();
        context.setState(new StateSetTime());
    }

    //Tænder radioen
    @Override
    public void onClick_Power(ContextClockradio context) {
        context.setState(StateRadioOn.getInstant());
    }
}
