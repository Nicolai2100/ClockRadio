package dk.dtu.philipsclockradio;

import java.util.Calendar;
import java.util.Date;

public class ContextClockradio {
    private State currentState;
    private Date mTime;
    private Double mRadioChannel;
    private String mRadioFrequency;
    private String mDisplayText;
    public boolean isClockRunning = false;

    public static MainUI ui;

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

    //Opdaterer kontekst time state og UI
    void setTime(Date time) {
        mTime = time;
        if (currentState.getClass().getSimpleName().equals("StateStandby")) {
            updateDisplayTime();
        }
    }

    void updateDisplayTime() {
        mDisplayText = mTime.toString().substring(11, 16);
        ui.setDisplayText(mDisplayText);
    }

    //Opdaterer kontekst time state og UI
    void setRadio(Double radioChannel, String radioFrequency) {
        this.mRadioChannel = radioChannel;
        this.mRadioFrequency = radioFrequency;
        if (currentState.getClass().getSimpleName().equals("StateRadioOn")) {
            updateDisplayRadio();
        }
    }
    //todo display fm/am + frekvens
    void updateDisplayRadio() {
        mDisplayText = mRadioFrequency + " " + mRadioChannel.toString();
        ui.setDisplayText(mDisplayText);
    }

    public Date getTime() {
        return mTime;
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
}