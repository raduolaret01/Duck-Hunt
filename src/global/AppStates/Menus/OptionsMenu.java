package global.AppStates.Menus;

import global.*;
import global.Systems.Settings;
import global.Systems.TileFactory;
import global.Systems.Timer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class OptionsMenu extends Menu{

    private Settings tempSettings;
    private int resoulutionOpt;
    private boolean resChanged;
    private Tile backdropPanel = new Tile(70,698,370,524, 576);
    private Tile[] volumeDisplay = new Tile[3];

    @Override
    public void init() {

        tempSettings = new Settings(Application.getSettings());

        switch(tempSettings.getResolutionH()){
            case 1080:
                resoulutionOpt = 1;
                break;
            case 720:
                resoulutionOpt = 2;
                break;
            case 480:
                resoulutionOpt = 3;
                break;
            default:
                throw new IllegalStateException("Unimplemented resolution detected in options menu!");
        }
        resChanged = false;

        buttons = new Button[5];
        this.window = Application.getWindow();

        buttons[0] = TileFactory.MakeButton("Resolution" + resoulutionOpt,818, 426, 280,100);
        buttons[1] = TileFactory.MakeButton("LeftArrow", 870, 650, 16,32);
        buttons[2] = TileFactory.MakeButton("RightArrow", 1030, 650, 16,32);
        buttons[3] = TileFactory.MakeButton("ScoreDelete", 818, 750, 280, 104);
        buttons[4] = TileFactory.MakeButton("Back",1670,950,220,100);
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
                for(int i = 0; i < 5; ++i){
                    if(buttons[i].mouseOver(pointer.posX+25, pointer.posY+25)){
                        pressedButton = i;
                    }
                }
            }
        });

        updateVolumeDisplay(tempSettings.getVolume());
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

            for (int i = 0; i < 5; ++i) {
                buttons[i].draw();
            }

            for(int i = 0; i < 3; ++i){
                volumeDisplay[i].draw();
            }

            pointer.draw();

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events.
            glfwPollEvents();

            switch (pressedButton) {
                case -1:
                    break;
                case 0:
                    resoulutionOpt %= 3;
                    ++resoulutionOpt;
                    buttons[0] = TileFactory.MakeButton("Resolution" + resoulutionOpt,818, 426, 280,100);
                    resChanged = true;
                    switch (resoulutionOpt){
                        case 1:
                            tempSettings.setResolution(1920,1080);
                            break;
                        case 2:
                            tempSettings.setResolution(1280,720);
                            break;
                        case 3:
                            tempSettings.setResolution(848,480);
                            break;
                        default:
                            throw new IllegalStateException("???????how???????" + resoulutionOpt);
                    }
                    break;
                case 1:
                    tempSettings.lowerVolume(5);
                    updateVolumeDisplay(tempSettings.getVolume());
                    break;
                case 2:
                    tempSettings.increaseVolume(5);
                    updateVolumeDisplay(tempSettings.getVolume());
                    break;
                case 3:
                    return 5;
                case 4:

                    if(resChanged){
                        ResolutionConfirmMenu.setNewSettings(tempSettings);
                        return 4;
                    }
                    Application.updateSettings(tempSettings);
                    return Application.getLastState();
                default:
                    throw new IllegalStateException("Illegal pressedButton value at OptionMenu: " + pressedButton);
            }
            pressedButton = -1;
        }
        return 0;
    }

    private void updateVolumeDisplay(int vol){
        int tens = 1;
        for(int i = 2; i >= 0; --i){
            int temp = (vol / tens) % 10;
            volumeDisplay[i] = TileFactory.MakeSBTile((char) (temp + 48),910 + 32 * i,650,32,32);
            tens *= 10;
        }
    }
}
