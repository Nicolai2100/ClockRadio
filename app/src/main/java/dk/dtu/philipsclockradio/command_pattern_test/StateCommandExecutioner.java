package dk.dtu.philipsclockradio.command_pattern_test;

import java.util.ArrayList;
import java.util.List;

public class StateCommandExecutioner {
    private static StateCommandExecutioner instance = null;

    private StateCommandExecutioner(){ }

    public static StateCommandExecutioner getInstance() {
        if (instance == null){
            instance = new StateCommandExecutioner();
        }
        return instance;
    }

    private final List<Command> stateCommands = new ArrayList<>();

    public void executeOperation(Command stateCommand) {
        stateCommands.add(stateCommand);
        stateCommand.execute();
//        return stateCommand.execute();
        return;
    }

    /*    TextFileOperationExecutor textFileOperationExecutor
      = new TextFileOperationExecutor();
    textFileOperationExecutor.executeOperation(
              new OpenTextFileOperation(new TextFile("file1.txt"))));
    textFileOperationExecutor.executeOperation(
              new SaveTextFileOperation(new TextFile("file2.txt"))));
*/
}
