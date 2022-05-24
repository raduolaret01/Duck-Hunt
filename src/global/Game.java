package global;

import global.HudElements.ProgressTab;
import global.HudElements.RoundCounter;
import global.HudElements.ScoreTab;
import global.HudElements.ShotCounter;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Game extends ApplicationState {

    public static Level currentLevel;
    private static Level[] levels = new Level[3];

    public static void LoadLevel(int id){
        switch (id){
            case 1:
                currentLevel = new Level_1();
                break;
                //TODO: Implement the rest of the levels
            default:
                throw new IllegalStateException("Level " + id + " not implemented!");
        }
    }

    private static boolean exitFlag;

    public static void setExitFlag(boolean flag){
        exitFlag = flag;
    }

    @Override
    protected void init() {
        window = Application.getWindow();
        background = TileFactory.MakeBGTile(1);

        exitFlag = false;

        //currentLevel = new Level_1();



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
        });

        currentLevel.init();
    }

    @Override
    protected int loop() {
        // Set the clear color
        glClearColor(0.2f, 0.5f, 0.2f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE or N keys.
        while ( !glfwWindowShouldClose(window) && !exitFlag && !Level.GameOver()) {
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

        if(DataManager.checkScore(ScoreTab.getScore())){
            NewTopScoreMenu.setScore(ScoreTab.getScore());
            return 7;
        }
        return 6;
    }

}
