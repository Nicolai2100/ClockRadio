package dk.dtu.philipsclockradio;

import java.util.Date;


public class StateSetTime extends StateAdapter {
    Date mTime;

    StateSetTime(){}

    @Override
    public void onEnterState(ContextClockradio context) {
        mTime = context.getTime();
        context.ui.turnOnTextBlink();
        context.updateDisplayTime();
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        //Gets current timestamp (Date)
        mTime.setTime(mTime.getTime() + 3600000);
        context.setTime(mTime);
        context.updateDisplayTime();
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        //Gets current timestamp (Date)
        mTime.setTime(mTime.getTime() + 60000);
        context.setTime(mTime);
        context.updateDisplayTime();
    }

    @Override
    public void onClick_Preset(ContextClockradio context) {
        context.setState(new StateStandby(context.getTime()));
    }

}
