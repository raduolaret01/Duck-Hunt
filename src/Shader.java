
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Shader {

    final String vertexShaderSource = "#version 330 core\n" +
            "layout (location = 0) in vec3 aPos;\n" +
            "void main()\n" +
            "{\n" +
            "   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
            "}\0";
    int vertexShader = 0;
    void compileVertexShader(){
        vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,vertexShaderSource);
        glCompileShader(vertexShader);
        if(glGetShaderi(vertexShader,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.print("Vertex Shader Compilation Failed:\n" + glGetShaderInfoLog(vertexShader));
            throw new RuntimeException("Shader compilation failed!");
        }
    }

    final String fragmentShaderSource = "#version 330 core\n" +
            "out vec4 FragColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
            "} ";
    int fragmentShader = 0;
    void compileFragmentShader(){
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,fragmentShaderSource);
        glCompileShader(fragmentShader);
        if(glGetShaderi(fragmentShader,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.print("Fragment Shader Compilation Failed:\n" + glGetShaderInfoLog(fragmentShader));
            throw new RuntimeException("Shader compilation failed!");
        }
    }

    int shaderProgram = glCreateProgram();
    void linkShaders(){
        compileVertexShader();
        compileFragmentShader();

        glAttachShader(shaderProgram,vertexShader);
        glAttachShader(shaderProgram,fragmentShader);
        glLinkProgram(shaderProgram);

        if(glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE){
            System.err.print("Shader program linking failed!:\n" + glGetProgramInfoLog(shaderProgram));
            throw new RuntimeException("Shader linking failed!");
        }

        glUseProgram(shaderProgram);

        glDetachShader(shaderProgram,vertexShader);
        glDetachShader(shaderProgram,fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }
}
