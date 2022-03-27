import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;


public class GameObject {
    protected int posX, posY, width, height, textureID;

    GameObject(int texID){
        textureID = texID;
    }

    public void Draw(){

    }
}
