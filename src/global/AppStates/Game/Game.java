package global.AppStates.Game;

import global.*;
import global.AppStates.ApplicationState;
import global.AppStates.Game.Level.*;
import global.AppStates.Menus.NewTopScoreMenu;
import global.AppStates.Game.Level.HudElements.ScoreTab;
import global.Systems.DataManager;
import global.Systems.TileFactory;
import global.Systems.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Game extends ApplicationState {

    public static Level currentLevel;
    private static int currentLevelId;

    public static void LoadLevel(int id){
        currentLevelId = id;
        switch (id){
            case 1:
                currentLevel = new Level_1();
                break;
            case 2:
                currentLevel = new Level_2();
                break;
            case 3:
                currentLevel = new Level_3();
                break;
            default:
                throw new IllegalStateException("Level " + id + " not implemented!");
        }
    }

    private static boolean exitFlag;

    public static void setExitFlag(boolean flag){
        exitFlag = flag;
    }

    private Crosshair crosshair;

    @Override
    public void init() {
        window = Application.getWindow();
        background = TileFactory.MakeBGTile(currentLevelId);

        exitFlag = false;

        crosshair = new Crosshair();

        //Mouse callback to shoot on click
        if(currentLevelId == 2) {
            glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
                if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                    currentLevel.killAround(crosshair.posX + 17, crosshair.posY + 17);
                }
            });
        }
        else {
            glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
                if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                    currentLevel.killAround(pointer.posX + 25, pointer.posY + 25);
                }
            });
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }

        });

        currentLevel.init();
    }

    @Override
    public int loop() {
        // Set the clear color
        glClearColor(0.2f, 0.5f, 0.2f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE or N keys.
        while ( !glfwWindowShouldClose(window) && !exitFlag) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            //draw background
            background.draw();

            // Update ducks positions and states

            currentLevel.update();
            currentLevel.draw();

            pointer.draw();
            if(currentLevelId == 2) {
                crosshair.update();
                crosshair.draw();
            }


            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
        }

        if(DataManager.checkScore(ScoreTab.getScore())){
            NewTopScoreMenu.setScore(ScoreTab.getScore());
            return 7;
        }
        return 6;
    }

}
