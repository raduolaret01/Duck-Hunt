package global;

import global.HudElements.ProgressTab;
import global.HudElements.RoundCounter;
import global.HudElements.ScoreTab;
import global.HudElements.ShotCounter;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class Game extends ApplicationState {

    private boolean pauseFlag = false;
    private Duck testDuck[];
    /** 0 = Shots, 1 = Round Progress, 2 = Score, 3 = Round number, 4 = Dog (for now) */
    private GraphicObject[] HudObjects = new GraphicObject[4];

    //Information needed for Hud Elements
    static int ducksShot = 0;
    static int round = 1;

    public static int getDucksShot(){
        return ducksShot;
    }

    public static int getRound(){
        return round;
    }

    public static void advanceRound(){
        ++round;
    }

    @Override
    protected void init() {

        this.window = Application.getWindow();
        background = TileFactory.MakeTile("Level1Background",0,0,1920,1080);

        HudObjects[0] = new ShotCounter(224,938);
        HudObjects[1] = new ProgressTab(724,938);
        HudObjects[2] = new ScoreTab(1514,938);
        HudObjects[3] = new RoundCounter(224,838);

        exitFlag = false;
        pauseFlag = false;

        //Mouse callback to shoot on click
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->{
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                for(Duck ducky : testDuck){
                    if(Math.abs((pointer.posX + 25) - (ducky.posX + ducky.width / 2)) < 77 && Math.abs((pointer.posY + 25) - (ducky.posY + ducky.height / 2)) < 77){
                        ducky.kill();
                        ducksShot++;
                    }
                }
                HudObjects[0].update();
            }
        });

        // Instantiate ducks
        testDuck = new Duck[5];
        for(int i = 0; i < 5; ++i){
            testDuck[i] = new Duck();
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_Q && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
//            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
//                pauseFlag = true;
//            }
            if( key == GLFW_KEY_N && action == GLFW_RELEASE ) {
                exitFlag = true; // Leave global.Game if N is pressed
                System.out.println("N pressed!");
            }
        });
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

            //update Hud Elements
            for(int i = 1; i < 4; ++i){
                HudObjects[i].update();
                HudObjects[i].draw();
            }
            HudObjects[0].draw();

            ducksShot = 0;

            // Update ducks positions and states
            for(int i = 0; i < 5; ++i){
                testDuck[i].update();
                testDuck[i].draw();
                if(testDuck[i].posY > 1200){
                    testDuck[i] = new Duck();
                }
            }

            renderContext.DrawObject(pointer);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
        }
        if(pauseFlag){
            return 2;
        }
        round = 0;
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
