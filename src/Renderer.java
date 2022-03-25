import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Renderer {
    float[] vertices = {
            -0.5f, -0.5f,
            0.5f, -0.5f,
            -0.5f,  0.5f,
            0.5f, 0.5f
    };
    int[] indices = {
            0, 1, 2,
            1, 2, 3
    };
    int VBO, VAO, EBO;
    void Init(){
        VBO = glGenBuffers();
        EBO = glGenBuffers();
        VAO = glGenVertexArrays();
        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, EBO);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices,GL_STATIC_DRAW);
        glVertexAttribPointer(0,2,GL_FLOAT,false,2 * Float.BYTES,0);
        glEnableVertexAttribArray(0);
        //glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    }

    void Draw(){
        glBindVertexArray(VAO);
        //glDrawArrays(GL_TRIANGLES,0,3);
        glDrawElements(GL_TRIANGLES,6,GL_UNSIGNED_INT,0);
        glBindVertexArray(0);
    }

}
