import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;


public abstract class GameObject {
    protected int posX, posY, width, height, texID;

    GameObject(){ };

    GameObject(int textureID, int x, int y, int w, int h){
        texID = textureID;
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    public abstract void update();
}
