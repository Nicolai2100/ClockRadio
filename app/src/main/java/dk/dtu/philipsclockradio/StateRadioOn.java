package dk.dtu.philipsclockradio;

import java.util.ArrayList;
import java.util.Collections;

public class StateRadioOn extends StateAdapter {
    //todo am og fm frekvens gemmes og aktiveres når der skiftes
    private static StateRadioOn instant = null;

    private ArrayList<Double> radioChannelsList;

    private Double currentRadioChannel = null;
    private String currentRadioFrequency;
    private Double fmRadioChannel = 0.0;
    private Double amRadioChannel = 0.0;

    StateRadioOn() {
        radioChannelsList = getRadioChannelsList();

        if (currentRadioChannel == null) {
            currentRadioChannel = 0.0;
            fmRadioChannel = currentRadioChannel;
            currentRadioFrequency = "FM";
        }
    }

    public static StateRadioOn getInstant() {
        if (instant == null) {
            instant = new StateRadioOn();
        }
        return instant;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.setRadio(currentRadioChannel, currentRadioFrequency);
        /*
        mTime = context.getTime();
        context.ui.turnOnTextBlink();
        context.updateDisplayTime();
        */
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        context.setState(StateStandby.getInstance(context.getTime()));
    }

    //Skifter mellem AM og FM
    @Override
    public void onClick_Power(ContextClockradio context) {
        if (currentRadioFrequency.equalsIgnoreCase("FM")) {
            currentRadioFrequency = "AM";
            currentRadioChannel = amRadioChannel;
        } else {
            currentRadioFrequency = "FM";
            currentRadioChannel = fmRadioChannel;
        }
        context.setRadio(currentRadioChannel, currentRadioFrequency);
    }

    @Override
    public void onExitState(ContextClockradio context) {
        //context.ui.turnOffTextBlink();
    }

    //Søger efter næste kanal
    @Override
    public void onClick_Min(ContextClockradio context) {
        int index = radioChannelsList.indexOf(currentRadioChannel);
        double newChannel = 0;

        try {
            newChannel = radioChannelsList.get(index + 1);
            if (newChannel > currentRadioChannel) {
                currentRadioChannel = newChannel;
            } else
                currentRadioChannel = radioChannelsList.get(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(0);
        } catch (IndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(0);
        }

        context.setRadio(currentRadioChannel, currentRadioFrequency);
        context.updateDisplayRadio();
    }

    //Søger efter forrige kanal
    @Override
    public void onClick_Hour(ContextClockradio context) {
        int index = radioChannelsList.indexOf(currentRadioChannel);
        double newChannel;

        try {
            newChannel = radioChannelsList.get(index - 1);
            if (newChannel != 0) {
                currentRadioChannel = newChannel;
            } else
                currentRadioChannel = radioChannelsList.get(radioChannelsList.size() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(radioChannelsList.size() - 1);
        } catch (IndexOutOfBoundsException e) {
            currentRadioChannel = radioChannelsList.get(radioChannelsList.size() - 1);
        }

        if (currentRadioFrequency.equalsIgnoreCase("am")) {
            amRadioChannel = currentRadioChannel;
        } else {
            fmRadioChannel = currentRadioChannel;
        }

        context.setRadio(currentRadioChannel, currentRadioFrequency);
        context.updateDisplayRadio();

        //Gets current timestamp (Date)
       /*
       mTime.setTime(mTime.getTime() + 3600000);
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

    private ArrayList<Double> getRadioChannelsList() {
        radioChannelsList = new ArrayList<>();
        radioChannelsList.add(101.5);
        radioChannelsList.add(97.0);
        radioChannelsList.add(103.2);
        radioChannelsList.add(102.7);
        radioChannelsList.add(99.4);
        radioChannelsList.add(106.6);
        radioChannelsList.add(90.1);
        radioChannelsList.add(101.5);
        radioChannelsList.add(100.6);
        Collections.sort(radioChannelsList);
        return radioChannelsList;
    }
}
