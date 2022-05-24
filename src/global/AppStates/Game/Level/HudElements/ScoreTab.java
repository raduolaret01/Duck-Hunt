package global.AppStates.Game.Level.HudElements;

import global.*;
import global.AppStates.Game.Game;
import global.Systems.Renderer;
import global.Systems.TileFactory;

public class ScoreTab extends GraphicObject {

    public ScoreTab(int x, int y){
        super(43, x, y, 212, 84);
        score = 0;
    }

    private static int score = 0;
    private Tile[] scoreTiles = new Tile[6];

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
        for(Tile digit : scoreTiles){
            digit.draw();
        }
    }

    @Override
    public void update() {
        int amount = Game.currentLevel.getDucksShot() * 25;
        score+=amount;
        if(score > 999999){
            score = 999999;
        }
        int tens = 1;
        for(int i = 5; i >= 0; --i){
            Integer temp = (score / tens) % 10;
            scoreTiles[i] = TileFactory.MakeGameTile(temp.toString(),posX + 12 + 32 * i,posY + 12,32,32);
            tens *= 10;
        }
    }

    public static int getScore(){
        return score;
    }

}
