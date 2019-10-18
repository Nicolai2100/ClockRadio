package dk.dtu.philipsclockradio;

import java.util.HashMap;

public class StateRadioOn extends StateAdapter {
    private HashMap<Double, String> radioChannels;
    private Double currentRadioChannel = null;

    StateRadioOn() {
        radioChannels = getRadioChannels();
    }

    @Override
    public void onEnterState(ContextClockradio context) {

        if (currentRadioChannel == null) {
            currentRadioChannel = 0.0;
        }
        context.setRadioFrequency(currentRadioChannel);
        /*
        mTime = context.getTime();
        context.ui.turnOnTextBlink();
        context.updateDisplayTime();
        */
    }

    @Override
    public void onExitState(ContextClockradio context) {
        //context.ui.turnOffTextBlink();
    }


    @Override
    public void onClick_Hour(ContextClockradio context) {
        //Gets current timestamp (Date)
       /*
       mTime.setTime(mTime.getTime() + 3600000);
        context.setTime(mTime);
        context.updateDisplayTime();
        */
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        /*
        Gets current timestamp (Date)
        mTime.setTime(mTime.getTime() + 60000);
        context.setTime(mTime);
        context.updateDisplayTime();
        */
    }

    @Override
    public void onClick_Preset(ContextClockradio context) {
/*
        context.setState(new StateStandby(context.getTime()));
*/
    }

    private HashMap<Double, String> getRadioChannels() {
        radioChannels = new HashMap<>();
        radioChannels.put(101.5, "DR P1");
        radioChannels.put(97.0, "DR P2");
        radioChannels.put(103.2, "DR P3");
        radioChannels.put(102.7, "DR P4 KBH");
        radioChannels.put(99.4, "DR P5");
        radioChannels.put(106.6, "DR P7 BEAR");
        radioChannels.put(90.1, "DR P7 MIX");
        radioChannels.put(101.5, "DR P8 JAZZ");
        radioChannels.put(100.6, "RADIO 24/7");
        return radioChannels;
    }

}
