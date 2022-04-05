import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

    // The window handle
    private long window;
    public void setWindow(long win){
        this.window = win;
    }
    private boolean exitFlag = false;
    private Renderer renderContext = Renderer.getInstance();
    private Cursor pointer = Cursor.getInstance();
    private Duck testDuck[];

    public void run() {

        init();
        loop();

    }

    protected void init() {
        //Cursor shoot on click callback
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->{
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                for(Duck ducky : testDuck){
                    if(Math.abs(pointer.posX - (ducky.posX + ducky.width / 2)) < 77 && Math.abs(pointer.posY - (ducky.posY + ducky.height / 2)) < 77){
                        ducky.kill();
                    }
                }
            }
        });

        testDuck = new Duck[5];
        for(int i = 0; i < 5; ++i){
            testDuck[i] = new Duck();
        }
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
            if( key == GLFW_KEY_N && action == GLFW_RELEASE ) {
                exitFlag = true;
                System.out.println("N pressed!");
            }
        });
    }

    protected void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.

        // Set the clear color
        glClearColor(0.2f, 0.5f, 0.2f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) && !exitFlag ) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            for(int i = 0; i < 5; ++i){
                testDuck[i].update();
                renderContext.DrawObject(testDuck[i]);
                if(testDuck[i].posY > 1200){
                    testDuck[i] = new Duck();
                }
            }

            renderContext.DrawObject(pointer);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }

        exitFlag = false;
    }
}
