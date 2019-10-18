package dk.dtu.philipsclockradio;

public interface State {
    void onEnterState(ContextClockradio context);
    void onExitState(ContextClockradio context);
    void onClick_Hour(ContextClockradio context);
    void onClick_Min(ContextClockradio context);
    void onClick_Preset(ContextClockradio context);
    void onClick_Power(ContextClockradio context);
    void onClick_Sleep(ContextClockradio context);
    void onClick_AL1(ContextClockradio context);
    void onClick_AL2(ContextClockradio context);
    void onClick_Snooze(ContextClockradio context);
    void onLongClick_Hour(ContextClockradio context);
    void onLongClick_Min(ContextClockradio context);
    void onLongClick_Preset(ContextClockradio context);
    void onLongClick_Power(ContextClockradio context);
    void onLongClick_Sleep(ContextClockradio context);
    void onLongClick_AL1(ContextClockradio context);
    void onLongClick_AL2(ContextClockradio context);
    void onLongClick_Snooze(ContextClockradio context);
}