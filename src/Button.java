public class Button extends GameObject{

    public Button(int x, int y, int w, int h){
        super(4, x, y, w, h);
    }

    @Override
    public void update() {    }

    public boolean mouseOver(int cursorX, int cursorY){
        return ((cursorX>posX && cursorX < posX + width) && (cursorY > posY && cursorY < posY + height));
    }
}
