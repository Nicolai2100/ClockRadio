package dk.dtu.philipsclockradio;

public class StateRadioStoreStations extends StateAdapter {
    private static StateRadioStoreStations instant = null;

    private StateRadioOn stateRadioOn;
    private double currentRadioChannel;
    private double[] savedChannels;
    private int pointer;

    private StateRadioStoreStations(StateRadioOn stateRadioOn, double currentRadioChannel) {
        this.stateRadioOn = stateRadioOn;
        this.currentRadioChannel = currentRadioChannel;
        savedChannels = new double[20];
    }

    public static StateRadioStoreStations getInstant(StateRadioOn stateRadioOnInstant, double currentRadioChannel) {
        if (instant == null) {
            instant = new StateRadioStoreStations(stateRadioOnInstant, currentRadioChannel);
        }
        return instant;
    }

    @Override
    public void onEnterState(ContextClockradio context) {
        context.ui.turnOnTextBlink();
        context.updateDisplaySimpleString("" + (pointer + 1));
    }

    @Override
    public void onExitState(ContextClockradio context) {
        super.onExitState(context);
        context.ui.turnOffTextBlink();
    }

    @Override
    public void onLongClick_Preset(ContextClockradio context) {
        savedChannels[pointer] = currentRadioChannel;
        context.setState(StateRadioOn.getInstant());
    }

    @Override
    public void onClick_Preset(ContextClockradio context) {
        ++pointer;
        if (pointer > 19) {
            pointer = 0;
        }
        String text = ""+(pointer + 1);
        context.updateDisplaySimpleString(text);
    }
}
