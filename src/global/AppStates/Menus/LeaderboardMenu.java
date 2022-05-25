package global.AppStates.Menus;

import global.*;
import global.Systems.DataManager;
import global.Systems.DataStructures.ScoreEntry;
import global.Systems.MyException;
import global.Systems.TileFactory;
import global.Systems.Timer;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class LeaderboardMenu extends Menu {

    private Tile backdropPanel = new Tile(69,708,400,504, 440);
    private ArrayList<ScoreEntry> scoreTable;
    private Tile[][] boardTiles;

    @Override
    public void init() {
        buttons = new Button[1];
        this.window = Application.getWindow();
        scoreTable = DataManager.getScores();
        boardTiles = new Tile[DataManager.getNumberOfScoreEntries()][12];
        createTable();

        buttons[0] = TileFactory.MakeButton("Back",870, 820, 180,100);
        background = TileFactory.MakeBGTile((int)(Math.random() * 2d) + 1);
        title = new Tile(131, 600,80,800,300);

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
                if(buttons[0].mouseOver(pointer.posX+25, pointer.posY+25)){
                    pressedButton = 0;
                }
            }
        });
    }

    @Override
    public int loop() throws MyException {
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

            if(boardTiles != null) {
                for (Tile[] tileSet : boardTiles) {
                    for (Tile tile : tileSet) {
                        if (tile != null) {
                            tile.draw();
                        }
                    }
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
                    return Application.getLastState();
                default:
                    throw new MyException("Illegal pressedButton value at NewTopScoreMenu: " + pressedButton);
            }
        }
        return 0;
    }

    private void createTable(){
        if(scoreTable != null) {
            for (int i = 0; i < boardTiles.length; ++i) {
                for (int j = 0; j < scoreTable.get(i).name.length(); ++j) {
                    boardTiles[i][j] = TileFactory.MakeSBTile((char) (scoreTable.get(i).name.charAt(j) + 32), 760 + 32 * j, 480 + 32 * i, 32, 32);
                }
                int tens = 1;
                for (int j = 11; j >= 6; --j) {
                    int temp = (scoreTable.get(i).score / tens) % 10;
                    boardTiles[i][j] = TileFactory.MakeSBTile((char) (temp + 48), 776 + 32 * j, 480 + 32 * i, 32, 32);
                    tens *= 10;
                }
            }
        }
        else {
            for(int i = 0; i < boardTiles.length; ++i) {
                for(int j = 0 ; j < 12; ++j){
                    boardTiles[i][j] = null;
                }
            }
        }
    }
}
