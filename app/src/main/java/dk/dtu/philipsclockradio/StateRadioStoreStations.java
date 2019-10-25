package dk.dtu.philipsclockradio;

public class StateRadioStoreStations extends StateAdapter {
    private static StateRadioStoreStations instance = null;

    private double currentRadioChannel;
    private double[] savedChannels;
    private int pointer;

    private StateRadioStoreStations(double currentRadioChannel) {
        this.currentRadioChannel = currentRadioChannel;
        savedChannels = new double[20];
    }

    public static StateRadioStoreStations getInstant(double currentRadioChannel) {
        if (instance == null) {
            instance = new StateRadioStoreStations(currentRadioChannel);
        }
        return instance;
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
        context.setState(StateRadioOn.getInstance());
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
