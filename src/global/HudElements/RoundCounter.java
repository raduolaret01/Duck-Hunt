package global.HudElements;

import global.*;
public class RoundCounter extends GraphicObject {

    public RoundCounter(int x, int y, int w, int h){
        super(47, x, y, w, h);
    }

    public RoundCounter(int x, int y){
        super(47,x,y,(int) (38f * (Renderer.getInstance().getUpscaleFactor())), (int) (12f * Renderer.getInstance().getUpscaleFactor()));
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
        round = Game.getRound();
        float upFact = Renderer.getInstance().getUpscaleFactor();
        int tens = 1;
        for(int i = 1; i >= 0; --i) {
            Integer temp = (round / tens) % 10;
            roundTiles[i] = TileFactory.MakeTile(temp.toString(), posX + (int)(19f * upFact) + i * (int)(8f * upFact), posY + (int)(2f * upFact), (int)(8f * upFact), (int)(8f * upFact));
            tens *= 10;
        }
    }
}
