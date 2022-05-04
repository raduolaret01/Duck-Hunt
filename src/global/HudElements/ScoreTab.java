package global.HudElements;

import global.*;
public class ScoreTab extends GraphicObject {

    public ScoreTab(int x, int y, int w, int h){
        super(44, x, y, w, h);
    }

    //Default size: 212 * 84
    public ScoreTab(int x, int y){
        super(44, x, y, 212, 84);
    }

    private int score = 0;
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
        int amount = Game.getDucksShot() * 25;
        score+=amount;
        if(score > 999999){
            score = 999999;
        }
        int tens = 1;
        for(int i = 5; i >= 0; --i){
            Integer temp = (score / tens) % 10;
            scoreTiles[i] = TileFactory.MakeTile(temp.toString(),posX + 12 + 32 * i,posY + 12,32,32);
            tens *= 10;
        }
    }

}
