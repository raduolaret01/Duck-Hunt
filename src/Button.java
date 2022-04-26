public class Button extends GraphicObject {

    public Button(int x, int y, int w, int h){
        super(43, x, y, w, h);
    }

    @Override
    public void draw() {

    }

    @Override
    public void update() {    }

    public boolean mouseOver(int cursorX, int cursorY){
        return ((cursorX>posX && cursorX < posX + width) && (cursorY > posY && cursorY < posY + height));
    }
}
