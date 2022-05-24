package global.AppStates.Menus;

import global.GraphicObject;
import global.Systems.Renderer;

public class Button extends GraphicObject {

    public Button(int tex ,int x, int y, int w, int h){
        super(tex, x, y, w, h);
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
    }

    @Override
    public void update() {    }

    public boolean mouseOver(int cursorX, int cursorY){
        return ((cursorX>posX && cursorX < posX + width) && (cursorY > posY && cursorY < posY + height));
    }
}
