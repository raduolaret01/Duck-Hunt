
public class Cursor extends GameObject{

    private static Cursor instance = new Cursor();

    public static Cursor getInstance() {
        return instance;
    }
    private Cursor(){
        super(3, 1920/2, 1080/2, 50,50);
    }

    public void update() { }

    public void update(double x, double y) {
        posX = (int) x - 25;
        posY = (int) y - 25;
    }
}
