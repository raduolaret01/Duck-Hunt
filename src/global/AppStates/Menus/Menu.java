package global.AppStates.Menus;

import global.AppStates.ApplicationState;
import global.Tile;

public abstract class Menu extends ApplicationState {
    protected Tile title;
    protected Button[] buttons;
    int pressedButton = -1;

}
