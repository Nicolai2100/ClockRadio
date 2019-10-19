package dk.dtu.philipsclockradio;

public class StateAlarmSettingMode extends StateAdapter {
    private static StateAlarmSettingMode instance = null;
    private boolean alarmOn;
    private int hour = 00;
    private int minutte = 00;


    public static StateAlarmSettingMode getInstance() {
        if (instance == null) {
            instance = new StateAlarmSettingMode();
        }
        return instance;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnTextBlink();
        context.showDisplayFrequencyRadio(hour+":"+minutte);
    }

    @Override
    public void onExitState(ContextClockradio context) {
        context.ui.turnOffTextBlink();
    }

    @Override
    public void onClick_Hour(ContextClockradio context) {
        ++hour;
        if (hour > 23){
            hour = 00;
        }
        context.showDisplayFrequencyRadio(hour+":"+minutte);
    }

    @Override
    public void onClick_Min(ContextClockradio context) {
        ++minutte;
        if (minutte > 59){
            minutte = 00;
            ++hour;
        }
        context.showDisplayFrequencyRadio(hour+":"+minutte);
    }

    @Override
    public void onLongClick_AL1(ContextClockradio context) {
        //alarm on
        alarmOn = true;
        context.ui.turnOnLED(2);
        context.setState(StateStandby.getInstance(context.getTime()));
    }
}
