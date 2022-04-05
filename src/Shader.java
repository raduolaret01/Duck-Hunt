
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL33.*;

//I hate this code
//Update: I hate it less

public class Shader {

    int shaderProgramID;

    public Shader() throws IOException {
        String vsS = Files.readString(Path.of("rsc/shaders/vertex"));

        String fsS = Files.readString(Path.of("rsc/shaders/fragment"));

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader,vsS);
        glCompileShader(vertexShader);

        if(glGetShaderi(vertexShader,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.print("Vertex Shader Compilation Failed:\n" + glGetShaderInfoLog(vertexShader));
            throw new RuntimeException("Shader compilation failed!");
        }

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader,fsS);
        glCompileShader(fragmentShader);

        if(glGetShaderi(fragmentShader,GL_COMPILE_STATUS) == GL_FALSE){
            System.err.print("Fragment Shader Compilation Failed:\n" + glGetShaderInfoLog(fragmentShader));
            throw new RuntimeException("Shader compilation failed!");
        }

        shaderProgramID = glCreateProgram();

        glAttachShader(shaderProgramID,vertexShader);
        glAttachShader(shaderProgramID,fragmentShader);
        glLinkProgram(shaderProgramID);

        if(glGetProgrami(shaderProgramID, GL_LINK_STATUS) == GL_FALSE){
            System.err.print("Shader program linking failed!:\n" + glGetProgramInfoLog(shaderProgramID));
            throw new RuntimeException("Shader linking failed!");
        }

        glUseProgram(shaderProgramID);

        glDetachShader(shaderProgramID,vertexShader);
        glDetachShader(shaderProgramID,fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    void use(){
        glUseProgram(shaderProgramID);
    }

}
