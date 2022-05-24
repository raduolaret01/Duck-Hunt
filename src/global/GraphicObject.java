package global;

public abstract class GraphicObject {
    public int posX;
    public int posY;
    public int width;
    public int height;
    public int texID;

    GraphicObject(){ };

    public GraphicObject(int textureID, int x, int y, int w, int h){
        texID = textureID;
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    public abstract void draw();

    public abstract void update();
}
