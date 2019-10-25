package dk.dtu.philipsclockradio.command_pattern_test;

import dk.dtu.philipsclockradio.ContextClockradio;
import dk.dtu.philipsclockradio.State;

public class OnClick_SleepOperation implements Command {

    private State state;
    private ContextClockradio context;

    public OnClick_SleepOperation(State state, ContextClockradio context){
        this.state = state;
        this.context = context;
    }

    @Override
    public void execute() {
        state.onClick_Sleep(context);
    }
}
