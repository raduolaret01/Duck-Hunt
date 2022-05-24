package global.AppStates.Menus;

import global.*;
import global.Systems.DataManager;
import global.Systems.DataStructures.ScoreEntry;
import global.Systems.TileFactory;
import global.Systems.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class NewTopScoreMenu extends Menu {

    private Tile backdropPanel = new Tile(68,704,400,512,412);
    private Tile[] scoreTiles = new Tile[6];
    private Tile[] nameTiles = new Tile[6];
    private StringBuilder name = new StringBuilder(6);

    private static int score;

    public static void setScore(int sc){
        score = sc;
    }

    @Override
    public void init() {
        buttons = new Button[1];
        this.window = Application.getWindow();
        name.delete(0,name.length());

        buttons[0] = TileFactory.MakeButton("Yes",870, 720, 180,100);
        background = TileFactory.MakeBGTile((int)(Math.random() * 2d) + 1);
        title = new Tile(129, 585,140,750,150);

        pressedButton = -1;

        int tens = 1;
        for(int i = 5; i >= 0; --i){
            int temp = (score / tens) % 10;
            scoreTiles[i] = TileFactory.MakeSBTile((char)(temp + 48), 852 + 32 * i,488,32,32);
            tens *= 10;
        }

        for(int i = 0; i < 6; ++i){
            nameTiles[i] = null;
        }

        // Setup key callbacks.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if(action == GLFW_RELEASE) {
                if (key == GLFW_KEY_ESCAPE) {
                    glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
                }
                if(key >= GLFW_KEY_A && key <= GLFW_KEY_Z);{
                    updateName(key);
                }
                if(key == GLFW_KEY_BACKSPACE){
                    updateName(-1);
                }
            }
        });

        // Setup button click callback
        glfwSetMouseButtonCallback(window, (window, button, action, mods) ->{
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS){
                if(buttons[0].mouseOver(pointer.posX+25, pointer.posY+25)){
                    pressedButton = 0;
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
        while (!glfwWindowShouldClose(window)) {
            Timer.setDeltaTime();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
            background.draw();
            backdropPanel.draw();
            title.draw();

            buttons[0].draw();

            for(int i = 0 ; i < 6; ++i){
                scoreTiles[i].draw();
                if(nameTiles[i] != null) {
                    nameTiles[i].draw();
                }
            }

            pointer.draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();
            switch (pressedButton) {
                case -1:
                    break;
                case 0:
                    DataManager.InsertNewScore(new ScoreEntry(name.toString(),score));
                    return 6;
                default:
                    throw new IllegalStateException("Illegal pressedButton value at NewTopScoreMenu: " + pressedButton);
            }
        }
        return 0;
    }

    private void updateName(int ch){
        int i = name.length();
        if(ch == -1){
            name.deleteCharAt(i - 1);
            nameTiles[i-1] = null;
        }
        else if(ch >= 65 && ch <= 90 && name.length() < 6){
            name.append((char)ch);
            nameTiles[i] = TileFactory.MakeSBTile((char)(ch + 32),852 + 32 * i,628,32,32 );
        }
    }
}
