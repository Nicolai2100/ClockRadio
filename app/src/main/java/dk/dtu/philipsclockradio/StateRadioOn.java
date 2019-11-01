package dk.dtu.philipsclockradio;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class StateRadioOn extends StateAdapter {

    private static StateRadioOn instance = null;
    private ArrayList<Double> radioChannelsList;
    private double currentRadioChannel;
    private String currentRadioFrequency;
    private double fmRadioChannel;
    private double amRadioChannel = 0.0;

    private String SHARED_PREFS = "sharedPrefs";
    private String CURRENT_RADIOCHANNEL = "currentChannel";
    private String CURRENT_RADIOFREQUENCY = "currentFrequency";

    private StateRadioOn() {
        radioChannelsList = getRadioChannelsList();
        currentRadioChannel = 0.0;
        fmRadioChannel = currentRadioChannel;
        currentRadioFrequency = "FM";
        loadData();
    }

    public static StateRadioOn getInstance() {
        if (instance == null) {
            instance = new StateRadioOn();
        }
        return instance;
    }

    private void saveDate() {
        SharedPreferences sharedPreferences = MainUI.getContextOfApp().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(CURRENT_RADIOCHANNEL, (float) currentRadioChannel);
        editor.putString(CURRENT_RADIOFREQUENCY, currentRadioFrequency);
        editor.apply();

        Toast.makeText(MainUI.getContextOfApp(), "Data saved: " + CURRENT_RADIOCHANNEL, Toast.LENGTH_LONG).show();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = MainUI.getContextOfApp().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        currentRadioChannel = sharedPreferences.getFloat(CURRENT_RADIOCHANNEL, 0.0f);
        currentRadioFrequency = sharedPreferences.getString(CURRENT_RADIOFREQUENCY, "FM");
        Toast.makeText(MainUI.getContextOfApp(), "Data loaded" + CURRENT_RADIOCHANNEL, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onEnterState(ContextClockradio context) {
        context.setRadio(currentRadioChannel);
        context.updateDisplayRadioFrequency(currentRadioFrequency);
    }

    @Override
    public void onLongClick_Power(ContextClockradio context) {
        context.setState(StateStandby.getInstance(context.getTime()));
    }

    //Skifter mellem AM og FM
    @Override
    public void onClick_Power(ContextClockradio context) {
        if (currentRadioFrequency.equalsIgnoreCase("FM")) {
            fmRadioChannel = currentRadioChannel;
            currentRadioFrequency = "AM";
            currentRadioChannel = amRadioChannel;
        } else {
            amRadioChannel = currentRadioChannel;
            currentRadioFrequency = "FM";
            currentRadioChannel = fmRadioChannel;
        }
        context.setRadio(currentRadioChannel);
        context.updateDisplayRadioFrequency(currentRadioFrequency);
    }

    @Override
    public void onExitState(ContextClockradio context) {
        saveDate();
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        currentRadioChannel = currentRadioChannel + 1;
        if (currentRadioChannel > 108)
            currentRadioChannel = 0.0;
        context.setRadio(currentRadioChannel);
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        currentRadioChannel = currentRadioChannel - 1;
        if (currentRadioChannel < 0)
            currentRadioChannel = 108.0;
        context.setRadio(currentRadioChannel);
    }

    //Søger efter næste kanal
    @Override
    public void onLongClick_Min(ContextClockradio context) {
        currentRadioChannel = findNextChannel(radioChannelsList, currentRadioChannel);
        context.setRadio(currentRadioChannel);
    }

    //Søger efter forrige kanal
    @Override
    public void onLongClick_Hour(ContextClockradio context) {
        currentRadioChannel = findPreviousChannel(radioChannelsList, currentRadioChannel);
        context.setRadio(currentRadioChannel);
    }

    @Override
    public void onClick_Preset(ContextClockradio context) {
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

    public double findNextChannel(ArrayList<Double> channelList, double currentChannel) {

        int lastChannelIndex = channelList.size() - 1;
        double newCHannel = 0.0;

        if (currentChannel < channelList.get(0))
            return channelList.get(0);
        if (currentChannel >= channelList.get(lastChannelIndex))
            return channelList.get(0);

        for (int i = 0; i < lastChannelIndex; i++) {
            if (currentChannel >= channelList.get(i) && currentChannel < channelList.get(i + 1)) {
                newCHannel = channelList.get(i + 1);
                break;
            }
        }
        return newCHannel;
    }

    public double findPreviousChannel(ArrayList<Double> channelList, double currentChannel) {

        int lastChannelIndex = channelList.size() - 1;
        double newChannel = 0.0;

        if (currentChannel <= channelList.get(0))
            return channelList.get(lastChannelIndex);
        if (currentChannel > channelList.get(lastChannelIndex))
            return channelList.get(lastChannelIndex);

        for (int i = 0; i < lastChannelIndex; i++) {
            if (currentChannel > channelList.get(i) && currentChannel <= channelList.get(i + 1)) {
                newChannel = channelList.get(i);
                break;
            }
        }
        return newChannel;
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        context.setState(StateRadioStoreStations.getInstant(currentRadioChannel));
    }
}

