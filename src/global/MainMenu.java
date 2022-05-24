package global;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class MainMenu extends Menu{

    private Tile backdrop = new Tile(67, 750,390,420,500);

    @Override
    protected void init() {
        buttons = new Button[3];
        this.window = Application.getWindow();

        buttons[0] = TileFactory.MakeButton("PlayGame",850, 440, 220,100);
        buttons[1] = TileFactory.MakeButton("Options", 850, 590, 220,100);
        buttons[2] = TileFactory.MakeButton("Leaderboard", 850, 740, 220, 100);
        background = TileFactory.MakeBGTile((int)(Math.random() * 2d) + 1);
        title = new Tile(128, 660,80,600,300);

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
                for(int i = 0; i < 3; ++i){
                    if(buttons[i].mouseOver(pointer.posX+25, pointer.posY+25)){
                        pressedButton = i;
                    }
                }
            }
        });
    }

    @Override
    protected int loop() {
        // Set the clear color
        glClearColor(1f, 1f, 1f, 1.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            background.draw();
            backdrop.draw();
            title.draw();

            for(int i = 0; i < 3; ++i){
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
                    return 2;
                case 1:
                    return 3;
                case 2:
                    return 8;
                default:
                    throw new IllegalStateException("Illegal pressedButton value at MainMenu: " + pressedButton);
            }
        }
        return 0;
    }
}
