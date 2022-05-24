package global.AppStates.Game.Level.HudElements;

import global.*;
import global.AppStates.Game.Game;
import global.Systems.Renderer;
import global.Systems.TileFactory;

public class RoundCounter extends GraphicObject {

    //Default size: 152 * 48
    public RoundCounter(int x, int y){
        super(46,x,y,152, 48);
    }

    private int round;
    private Tile[] roundTiles = new Tile[2];

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
        for(Tile digit : roundTiles){
            digit.draw();
        }
    }

    @Override
    public void update() {
        round = Game.currentLevel.getRound();
        int tens = 1;
        for(int i = 1; i >= 0; --i) {
            Integer temp = (round / tens) % 10;
            roundTiles[i] = TileFactory.MakeGameTile(temp.toString(), posX + 76 + i * 32, posY + 8, 32, 32);
            tens *= 10;
        }
    }
}
