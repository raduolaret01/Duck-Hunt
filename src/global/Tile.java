package global;

public class Tile extends GraphicObject{
    //A bit forced, just a class to instantiate Graphic Objects.
    public Tile(int textureID, int x, int y, int w, int h){
        super(textureID, x, y, w, h);
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
    }

    public void drawAtDestination(int x, int y, int w, int h){
        Renderer.getInstance().DrawObject(new Tile(this.texID,x,y,w,h));
    }

    @Override
    public void update() {

    }
}
