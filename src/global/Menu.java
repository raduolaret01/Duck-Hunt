package global;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class Menu extends ApplicationState {

    private boolean loadGame = false;
    /** 0 = New global.Game, 1 = Top Scores / Leaderboard, 2 = Options */
    private Button[] buttons = new Button[3];
    int pressedButton = -1;

    @Override
    protected void init() {

        this.window = Application.getWindow();

        buttons[0] = new Button(860, 440, 200,100);

        loadGame = false;
        pressedButton = -1;

        // Setup key callbacks.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_Q && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }
            if( key == GLFW_KEY_M && action == GLFW_RELEASE ) {
                loadGame = true; // Enter global.Game if M is pressed
            }
        });

        // Setup button click callback
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->{
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                for(int i = 0; i < 3; ++i){
                    if(buttons[i]!=null && buttons[i].mouseOver(pointer.posX+25, pointer.posY+25)){
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
        while ( !glfwWindowShouldClose(window) && !loadGame) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


            for(int i = 0; i < 1; ++i){
                renderContext.DrawObject(buttons[i]);
            }

            renderContext.DrawObject(pointer);

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
            if(pressedButton == 0){
                loadGame = true;
            }
        }
        if(loadGame) {
            return 1;
        }
        return -1;
    }

    @Override
    protected int unloadState() {
        return 0;
    }
}
