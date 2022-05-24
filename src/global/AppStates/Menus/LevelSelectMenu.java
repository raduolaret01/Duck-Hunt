package global.AppStates.Menus;

import global.*;
import global.AppStates.Game.Game;
import global.Systems.TileFactory;
import global.Systems.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class LevelSelectMenu extends Menu {

    private Tile backdrop = new Tile(67, 750,390,420,500);

    @Override
    public void init() {

        buttons = new Button[4];
        this.window = Application.getWindow();

        buttons[0] = TileFactory.MakeButton("Level1",870, 440, 180,100);
        buttons[1] = TileFactory.MakeButton("Level2", 870, 590, 180,100);
        buttons[2] = TileFactory.MakeButton("Level3", 870, 740, 180, 100);
        buttons[3] = TileFactory.MakeButton("Back",1670,950,180,100);
        background = TileFactory.MakeBGTile((int)(Math.random() * 2d) + 1);
        title = new Tile(132, 510,80,900,300);

        pressedButton = -1;

        // Setup key callbacks.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
        });

        // Setup button click callback
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->{
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                for(int i = 0; i < 4; ++i){
                    if(buttons[i].mouseOver(pointer.posX+25, pointer.posY+25)){
                        pressedButton = i;
                    }
                }
            }
        });
    }

    @Override
    public int loop() {
        // Set the clear color
        glClearColor(1f, 1f, 1f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window)) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            background.draw();
            backdrop.draw();
            title.draw();

            for(int i = 0; i < 4; ++i){
                buttons[i].draw();
            }

            pointer.draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
            switch (pressedButton){
                case -1:
                    break;
                case 0:
                case 1:
                case 2:
                    Game.LoadLevel(pressedButton + 1);
                    return 1;
                case 3:
                    return Application.getLastState();
                default:
                    throw new IllegalStateException("Illegal pressedButton value at LevelSelectMenu: " + pressedButton);
            }
        }
        return 0;
    }
}
