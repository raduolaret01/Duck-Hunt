package global;

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
    int sheetW, sheetH, sheetChannels;


    public void testLoad(){
        /*
        Atlas = new global.Texture[30];
        Atlas[0] = new global.Texture(1,1,34,34);//duck flying
        Atlas[1] = new global.Texture(211, 1, 34, 34);//duck shot
        Atlas[2] = new global.Texture(246, 1, 34, 34);//duck falling
        Atlas[3] = new global.Texture(205, 360, 25, 25);//cursor
        Atlas[4] = new global.Texture(608,354,23,16);// temp button
        //global.Tile textures
        Atlas[5] = new global.Texture(1,106,256,240);//level 1 background
        Atlas[6] = new global.Texture(46,369,8,8);//0
        Atlas[7] = new global.Texture(55,369,8,8);//1
        Atlas[8] = new global.Texture(64,369,8,8);//2
        Atlas[9] = new global.Texture(73,369,8,8);//3
        Atlas[10] = new global.Texture(82,369,8,8);//4
        Atlas[11] = new global.Texture(46,378,8,8);//5
        Atlas[12] = new global.Texture(55,378,8,8);//6
        Atlas[13] = new global.Texture(64,378,8,8);//7
        Atlas[14] = new global.Texture(73,378,8,8);//8
        Atlas[15] = new global.Texture(82,378,8,8);//9
        Atlas[16] = new global.Texture(91,369,8,8);//white duck
        Atlas[17] = new global.Texture(91,378,8,8);//red duck
        Atlas[18] = new global.Texture(100,369,8,8);//white disk
        Atlas[19] = new global.Texture(100,378,8,8);//red disk
        Atlas[20] = new global.Texture(109,369,8,8);//shotgun cartridge
        Atlas[21] = new global.Texture(109,378,8,8);//blank tile
        //
        Atlas[22] = new global.Texture(151,347,53,21);//score tab
        Atlas[23] = new global.Texture(32,347,118,21);//progress tab
        Atlas[24] = new global.Texture(1,347,30,21);//Shots counter
        Atlas[25] = new global.Texture(205,347,38,12);//Round counter
        */

        Atlas = DataManager.LoadTextureInfo();


        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.callocInt(1);
            IntBuffer height = stack.callocInt(1);
            IntBuffer channels = stack.callocInt(1);
            spriteSheet = STBImage.stbi_load("rsc/textures/Untitled.png", width, height, channels, 4);
            if(spriteSheet == null){
                System.err.println("Error loading Image data!");
            }
            //System.out.println(channels.get(0));
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
//        for(int i = 0; i < 400;i+=4){
//            //System.out.println(duck.get(i) + " " + duck.get(i+1) + " " + duck.get(i+2) + " " + duck.get(i+3));
//            System.out.printf("%h,%h,%h,%h\n",duck.get(i),duck.get(i+1),duck.get(i+2),duck.get(i+3));
//        }
        //System.out.println(sheetW + " " + sheetH);
        //STBImage.stbi_image_free(duck);
    }

}
