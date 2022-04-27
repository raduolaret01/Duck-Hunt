package global.HudElements;

import global.*;
public class ScoreTab extends GraphicObject {

    public ScoreTab(int x, int y, int w, int h){
        super(44, x, y, w, h);
    }

    public ScoreTab(int x, int y){
        super(44, x, y, (int)(53f * Renderer.getInstance().getUpscaleFactor()), (int)(21f * Renderer.getInstance().getUpscaleFactor()));
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
        float upFact = Renderer.getInstance().getUpscaleFactor();
        int tens = 1;
        for(int i = 5; i >= 0; --i){
            Integer temp = (score / tens) % 10;
            scoreTiles[i] = TileFactory.MakeTile(temp.toString(),posX + (int)(3f * upFact) + (int)(8f * upFact) * i,posY + (int)(3f * upFact),(int)(8f * upFact),(int)(8f * upFact));
            tens *= 10;
        }
    }

}
