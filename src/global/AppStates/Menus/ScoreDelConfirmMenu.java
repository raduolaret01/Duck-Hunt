package global.AppStates.Menus;

import global.*;
import global.Systems.DataManager;
import global.Systems.TileFactory;
import global.Systems.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class ScoreDelConfirmMenu extends Menu {

    private Tile backdropPanel = new Tile(72,726,400,468, 256);

    @Override
    public void init() {

            buttons = new Button[2];
            this.window = Application.getWindow();

            buttons[0] = TileFactory.MakeButton("Yes",754, 532, 180,100);
            buttons[1] = TileFactory.MakeButton("No", 982, 532, 180,100);
            background = TileFactory.MakeBGTile((int)(Math.random() * 2d) + 1);
            title = new Tile(130, 510,80,900,150);

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
                    for(int i = 0; i < 2; ++i){
                        if(buttons[i].mouseOver(pointer.posX+25, pointer.posY+25)){
                            pressedButton = i;
                        }
                    }
                }
            });
        }

    @Override
    public int loop() {
        glClearColor(1f, 1f, 1f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            background.draw();
            backdropPanel.draw();
            title.draw();

            for (int i = 0; i < 2; ++i) {
                buttons[i].draw();
            }

            pointer.draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
            switch (pressedButton) {
                case -1:
                    break;
                case 0:
                    DataManager.DeleteScores();
                    return Application.getLastState();
                case 1:
                    return Application.getLastState();
                default:
                    throw new IllegalStateException("Illegal pressedButton value at ScoreDelConfirmMenu: " + pressedButton);
            }
        }
        return 0;
    }
}
