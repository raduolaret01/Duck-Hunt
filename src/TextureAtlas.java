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

    ByteBuffer duck = null;
    int duckW, duckH, duckCh;


    public void testLoad(){
        Atlas = new Texture[5];
        Atlas[0] = new Texture(1,1,34,34);
        Atlas[1] = new Texture(36, 1, 34, 34);
        Atlas[2] = new Texture(71, 1, 34, 34);
        Atlas[3] = new Texture(1, 37, 7, 7);
        Atlas[4] = new Texture(0,0,1,1);
        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.callocInt(1);
            IntBuffer height = stack.callocInt(1);
            IntBuffer channels = stack.callocInt(1);
            duck = STBImage.stbi_load("rsc/textures/Untitled.png", width, height, channels, 4);
            if(duck == null){
                System.err.println("Error loading Image data!");
            }
            System.out.println(channels.get(0));
            duckW = width.get(0);
            duckH = height.get(0);
        }
    }

    public float[] normalizeCoords(Texture tex){
        float[] temp = {
                (float) (tex.x)/duckW, (float) (tex.y)/duckH,
                (float) (tex.x+tex.w)/duckW, (float) (tex.y)/duckH,
                (float) (tex.x)/duckW, (float) (tex.y+tex.h)/duckH,
                (float) (tex.x+tex.w)/duckW, (float) (tex.y+tex.h)/duckH
        };
        return temp;
    }

    public void useAtlas(){
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, duckW, duckH, 0, GL_RGBA, GL_UNSIGNED_BYTE, duck);
//        for(int i = 0; i < 400;i+=4){
//            //System.out.println(duck.get(i) + " " + duck.get(i+1) + " " + duck.get(i+2) + " " + duck.get(i+3));
//            System.out.printf("%h,%h,%h,%h\n",duck.get(i),duck.get(i+1),duck.get(i+2),duck.get(i+3));
//        }
        System.out.println(duckW + " " + duckH);
        //STBImage.stbi_image_free(duck);
    }

}
