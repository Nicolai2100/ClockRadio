package dk.dtu.philipsclockradio.command_pattern_test;

import dk.dtu.philipsclockradio.ContextClockradio;
import dk.dtu.philipsclockradio.State;

public class OnEnterStateOperation implements Command {

    private State state;
    private ContextClockradio context;

    public OnEnterStateOperation(State state, ContextClockradio context){
        this.state = state;
        this.context = context;
    }

    @Override
    public void execute() {
        state.onEnterState(context);
    }
}
