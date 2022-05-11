package global;

import global.HudElements.ProgressTab;
import global.HudElements.RoundCounter;
import global.HudElements.ScoreTab;
import global.HudElements.ShotCounter;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Game extends ApplicationState {

    private boolean pauseFlag = false;
    public static Level currentLevel;
    private Level[] levels = new Level[3];

    @Override
    protected void init() {
        window = Application.getWindow();
        background = TileFactory.MakeGameTile("Level1Background",0,0,1920,1080);

        exitFlag = false;
        pauseFlag = false;

        levels[0] = new Level_1();
        currentLevel = levels[0];

        //Mouse callback to shoot on click
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->{
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                currentLevel.killAround(pointer.posX + 25, pointer.posY + 25);
            }
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_Q && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
//            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
//                pauseFlag = true;
//            }
            if ( key == GLFW_KEY_N && action == GLFW_RELEASE ) {
                exitFlag = true; // Leave global.Game if N is pressed
                System.out.println("N pressed!");
            }
            if ( key == GLFW_KEY_R && action == GLFW_RELEASE ) {
                Application.updateSettings(new Settings(1280,720,100));
            }
        });

        currentLevel.init();
    }

    @Override
    protected int loop() {
        // Set the clear color
        glClearColor(0.2f, 0.5f, 0.2f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE or N keys.
        while ( !glfwWindowShouldClose(window) && !exitFlag && !pauseFlag) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            //draw background
            background.draw();

            // Update ducks positions and states

            currentLevel.update();
            currentLevel.draw();

            renderContext.DrawObject(pointer);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
        }
        if(pauseFlag){
            return 2;
        }
        // Reset exit flag to be able to re-enter game later.
        // Could be raplaced by deleting and reinstantiating global.Game whenever we want to load another level
        exitFlag = false;
        return 0;
    }

    @Override
    protected int unloadState() {
        return 0;
    }
}
