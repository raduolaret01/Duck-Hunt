package global.AppStates;

import global.Cursor;
import global.Systems.MyException;
import global.Tile;

public abstract class ApplicationState {

    protected long window;

    protected Cursor pointer = Cursor.getInstance();
    protected Tile background;

    public abstract void init() throws MyException;
    public abstract int loop() throws MyException;
}
