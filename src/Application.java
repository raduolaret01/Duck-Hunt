
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

//TODO: Implement settings (and resolution)

public class Application {

    // The window handle
    private static long window;
    public static long getWindow(){
        return window;
    }

    private Renderer renderContext = Renderer.getInstance();
    private Cursor pointer = Cursor.getInstance();
    private ApplicationState[] states = new ApplicationState[5];
    private ApplicationState currentState, nextState;

    //Settings
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");


        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);//Set OpenGL version
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        //Use current monitor video mode for "borderless fullscreen"
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwWindowHint(GLFW_RED_BITS, vidmode.redBits());
        glfwWindowHint(GLFW_GREEN_BITS, vidmode.greenBits());
        glfwWindowHint(GLFW_BLUE_BITS, vidmode.blueBits());
        glfwWindowHint(GLFW_REFRESH_RATE, vidmode.refreshRate());
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will NOT be resizable (a few resolutions maybe if I implement options menu)

        //Default setting for now
        settings = new Settings(vidmode.width(), vidmode.height(),100);

        // Create the window
        window = glfwCreateWindow(settings.getResolutionW(), settings.getResolutionH(), "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        //Cursor position callback (custom cursor object)
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            pointer.update(xpos, ypos);
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        // Enable v-sync
        glfwSwapInterval(1);
        // Enable transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Make the window visible
        glfwShowWindow(window);

        //Initialise renderer
        renderContext.init(vidmode.width(), vidmode.height());

        states[1] = new Game();
        states[0] = new Menu();
        currentState = states[0];

        Timer.startTime();
    }

    private void loop() {
        // Set the clear color
        glClearColor(1f, 1f, 1f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            renderContext.DrawObject(pointer);

            currentState.init();

            nextState = states[ currentState.loop() ];

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();

            currentState = nextState;
            nextState = null;
        }
    }
//
//    protected void loadGame(){
//        game.init();
//        game.loop();
//
//        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
//            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
//                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
//            }
//            if( key == GLFW_KEY_M && action == GLFW_RELEASE ) {
//                System.out.println("M pressed!");
//                loadGame();
//            }
//        });
//        glfwSetMouseButtonCallback(window, null);
//
//        glClearColor(1f, 1f, 1f, 1.0f);
//    }

    private void changeState(){

    }

    public static int getWindowHeight(){
        int h;
        try(MemoryStack stack = stackPush()){
            IntBuffer height = stack.mallocInt(1);
            glfwGetWindowSize(window, null, height);
            h = height.get(0);
        }
        return h;
    }

    public static int getWindowWidth(){
        int w;
        try(MemoryStack stack = stackPush()){
            IntBuffer width = stack.mallocInt(1);
            glfwGetWindowSize(window, width, null);
            w = width.get(0);
        }
        return w;
    }


    public static void main(String[] args) {
        new Application().run();
    }



}
