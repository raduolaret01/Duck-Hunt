package global.Systems;

import global.GraphicObject;

import java.io.IOException;

import static org.lwjgl.opengl.GL33.*;


public class Renderer {
    //Singleton
    private static Renderer instance = new Renderer();
    private Renderer(){};
    public static Renderer getInstance() {
        return instance;
    }

    Shader shader = null;
    TextureAtlas TexAt = null;
    int screenW, screenH;

    float[] vertices = new float[16];
    final int[] indices = {
            0, 1, 2,
            1, 2, 3
    };
    int VBO, VAO, EBO, texture;
    public void init(int scW, int scH){

        try {
            shader = new Shader();
        } catch (IOException e) {
            e.printStackTrace();
        }

        shader.use();

        TexAt = new TextureAtlas();
        TexAt.Load();

        screenW = scW;
        screenH = scH;

        VBO = glGenBuffers();
        EBO = glGenBuffers();
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices,GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0,2,GL_FLOAT,false,4 * Float.BYTES,0);
        glVertexAttribPointer(1,2,GL_FLOAT,false,4 * Float.BYTES,2 * Float.BYTES);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        TexAt.useAtlas();

        //glUniform1i(glGetUniformLocation(shader.shaderProgramID, "global.Systems.DataStructures.Texture"), 1  );
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);

        //System.out.println( glGetProgramInfoLog(shader.shaderProgramID));
    }

    public void DrawObject(GraphicObject obj){
        float[] objV, texC;
        objV = normalizeObjectCoords(obj);
        texC = TexAt.normalizeCoords(TexAt.getAtlas()[obj.texID]);
        //Build VBO
        for(int i = 0,j = 0;i<8 && j<16;i+=2,j+=4){
            vertices[j] = objV[i];
            vertices[j+1] = objV[i+1];
            vertices[j+2] = texC[i];
            vertices[j+3] = texC[i+1];
        }

        //Buffer Data Streaming through re-specification (planned) (MAYBE)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        glBindVertexArray(VAO);
        glBindTexture(GL_TEXTURE_2D, texture);
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }
    public void DrawObjectFlip(GraphicObject obj, boolean flip){
        float[] objV, texC;
        objV = normalizeObjectCoords(obj);
        texC = TexAt.normalizeCoords(TexAt.getAtlas()[obj.texID]);
        if(flip){
            float temp = texC[0];
            texC[0] = texC[2];
            texC[2] = temp;
            temp = texC[4];
            texC[4] = texC[6];
            texC[6] = temp;
        }
        //Build VBO
        for(int i = 0,j = 0;i<8 && j<16;i+=2,j+=4){
            vertices[j] = objV[i];
            vertices[j+1] = objV[i+1];
            vertices[j+2] = texC[i];
            vertices[j+3] = texC[i+1];
        }

        //Buffer Data Streaming through re-specification (planned) (MAYBE) (HIGHLY UNLIKELY)
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

        glBindVertexArray(VAO);
        glBindTexture(GL_TEXTURE_2D, texture);
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }

    float[] normalizeObjectCoords(GraphicObject x){
        float halfW = (float) screenW / 2, halfH = (float) screenH / 2;
        float[] temp = {
                (float) (x.posX - halfW)/halfW, (float) (halfH - x.posY)/halfH,
                (float) (x.posX + x.width - halfW)/halfW, (float) (halfH - x.posY)/halfH,
                (float) (x.posX - halfW)/halfW, (float) (halfH - x.posY - x.height)/halfH,
                (float) (x.posX + x.width - halfW)/halfW, (float) (halfH - x.posY - x.height)/halfH
        };
        return temp;
    }

}
