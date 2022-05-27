package global;

import global.AppStates.*;
import global.AppStates.Game.Game;
import global.AppStates.Menus.*;
import global.Systems.*;
import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Application {

    // The window handle
    private static long window;
    public static long getWindow(){
        return window;
    }

    private Renderer renderContext = Renderer.getInstance();
    private Cursor pointer = Cursor.getInstance();
    private ApplicationState[] states = new ApplicationState[9];
    private static int currentState, nextState, lastState;

    public static int getLastState(){
        return lastState;
    }

    //global.Systems.Settings
    private static Settings settings;

    public static Settings getSettings() {
        return settings;
    }

    //Update settings
    public static void updateSettings(Settings settings) {
        glfwSetWindowSize(window, settings.getResolutionW(), settings.getResolutionH());
        glViewport(0,0, settings.getResolutionW(), settings.getResolutionH());
        Application.settings = settings;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        try {
            init();
            loop();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            DataManager.SaveScores();
            DataManager.SaveSettings();
        }
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() throws MyException {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new MyException("Unable to initialize GLFW");


        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);//Set OpenGL version
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        //
        glfwWindowHint(GLFW_CENTER_CURSOR, GLFW_TRUE);
        glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
        //Use current monitor video mode for "borderless fullscreen"
//        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
//        glfwWindowHint(GLFW_RED_BITS, vidmode.redBits());
//        glfwWindowHint(GLFW_GREEN_BITS, vidmode.greenBits());
//        glfwWindowHint(GLFW_BLUE_BITS, vidmode.blueBits());
//        glfwWindowHint(GLFW_REFRESH_RATE, vidmode.refreshRate());
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will NOT be resizable (a few resolutions maybe if I implement options menu)

        //Default setting for now
        //settings = new Settings(vidmode.width(), vidmode.height(),100);
        settings = DataManager.LoadSetttings();

        // Create the window
        window = glfwCreateWindow(settings.getResolutionW(), settings.getResolutionH(), "Hello World!", glfwGetPrimaryMonitor(), NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        //Use GLFW virtual cursor, as hardware cursor would switch coordinate systems on resolution change
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        //global.Cursor position callback
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
            //If cursor goes out of window, bring it back
            if (xpos < 0d) {
                xpos = 0d;
            } else if (xpos > 1920d) {
                xpos = 1920d;
            }
            if (ypos < 0d) {
                ypos = 0d;
            } else if (ypos > 1080d) {
                ypos = 1080d;
            }
            glfwSetCursorPos(window,xpos,ypos);
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
        glViewport(0,0, settings.getResolutionW(), settings.getResolutionH());

        renderContext.init(1920, 1080);

        states[0] = new MainMenu();
        states[1] = new Game();
        states[2] = new LevelSelectMenu();
        states[3] = new OptionsMenu();
        states[4] = new ResolutionConfirmMenu();
        states[5] = new ScoreDelConfirmMenu();
        states[6] = new GameOverMenu();
        states[7] = new NewTopScoreMenu();
        states[8] = new LeaderboardMenu();
        currentState = 0;

        updateSettings(settings);
        DataManager.Init();
        Timer.startTime();
    }

    private void loop() throws MyException {
        // Set the clear color
        glClearColor(1f, 1f, 1f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            pointer.draw();

            states[currentState].init();

            nextState = states[currentState].loop();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();

            if(currentState == 5 || currentState == 4) {
                lastState = 0;
            }
            else if(currentState == 7){
                lastState = 1;
            }
            else {
                lastState = currentState;
            }
            currentState = nextState;
            nextState = 0;
        }
    }

    public static void main(String[] args) {
        new Application().run();
    }



}
