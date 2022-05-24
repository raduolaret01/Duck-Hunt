package global.Systems;

import global.Systems.DataStructures.Texture;
import org.lwjgl.stb.*;
import org.lwjgl.system.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.opengl.GL33.*;

public class TextureAtlas {
    private Texture[] Atlas;

    public Texture[] getAtlas() {
        return Atlas;
    }

    ByteBuffer spriteSheet = null;
    int sheetW;
    int sheetH;


    public void Load(){

        Atlas = DataManager.LoadTextureInfo();

        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.callocInt(1);
            IntBuffer height = stack.callocInt(1);
            IntBuffer channels = stack.callocInt(1);
            spriteSheet = STBImage.stbi_load("rsc/textures/Untitled.png", width, height, channels, 4);
            if(spriteSheet == null){
                System.err.println("Error loading Image data!");
            }
            sheetW = width.get(0);
            sheetH = height.get(0);
        }
    }

    public float[] normalizeCoords(Texture tex){
        float[] temp = {
                (float) (tex.x)/ sheetW, (float) (tex.y)/ sheetH, // Top left
                (float) (tex.x+tex.w)/ sheetW, (float) (tex.y)/ sheetH, // Top right
                (float) (tex.x)/ sheetW, (float) (tex.y+tex.h)/ sheetH, // Bottom left
                (float) (tex.x+tex.w)/ sheetW, (float) (tex.y+tex.h)/ sheetH // Bottom right
        };
        return temp;
    }

    public void useAtlas(){
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, sheetW, sheetH, 0, GL_RGBA, GL_UNSIGNED_BYTE, spriteSheet);
        STBImage.stbi_image_free(spriteSheet);
    }

}
