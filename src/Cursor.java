//TODO: Can make Cursor extend Tile and replace the update(x,y) function with a drawAtDest override

public class Cursor extends GraphicObject {

    private static Cursor instance = new Cursor();

    public static Cursor getInstance() {
        return instance;
    }
    private Cursor(){
        super(3, 1920/2, 1080/2, 50,50);
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
    }

    public void update() { }

    public void update(double x, double y) {
        posX = (int) x - 25;
        posY = (int) y - 25;
    }
}
