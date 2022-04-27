package global.HudElements;


import global.*;

public class ProgressTab extends GraphicObject {

    public ProgressTab(int x, int y, int w, int h){
        super(45, x, y, w, h);
    }

    public ProgressTab(int x, int y){
        super(45, x, y, (int)(118f * Renderer.getInstance().getUpscaleFactor()), (int)(21f * Renderer.getInstance().getUpscaleFactor()));
    }
    private int maxDucks = 10;
    private int hitDucks = 0;
    private float timeElapsed = 0;
    private Tile[] duckTiles = new Tile[maxDucks];
    private Tile timerTile = TileFactory.MakeTile("Black", 0,0,8,8);

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
        float upFact = Renderer.getInstance().getUpscaleFactor();
        timerTile.drawAtDestination( posX + (int)(3f * upFact) + (int)(114f * upFact) - (int)(2f * upFact * timeElapsed),posY + (int)(13f * upFact), (int)(2f * upFact * timeElapsed), (int)(5f * upFact));

        for(Tile duck : duckTiles){
            duck.draw();
        }
    }

    @Override
    public void update() {
        timeElapsed += ((float) Timer.getDeltaTime()) / 1000f;
        if(timeElapsed >= 40f){
            timeElapsed -= 40f;
        }
        hitDucks += Game.getDucksShot();
        if(hitDucks >= maxDucks){
            hitDucks = 0;
            Game.advanceRound();
        }

        float upFact = Renderer.getInstance().getUpscaleFactor();
        int i = 0;
        while(i < hitDucks){// 35 3
            duckTiles[i] = TileFactory.MakeTile("DuckDead",posX + (int)(35f * upFact) + i * (int)(8f * upFact),posY + (int)(3f * upFact),(int)(8f * upFact),(int)(8f * upFact));
            ++i;
        }
        while(i < maxDucks){
            duckTiles[i] = TileFactory.MakeTile("DuckAlive",posX + (int)(35f * upFact) + i * (int)(8f * upFact),posY + (int)(3f * upFact),(int)(8f * upFact),(int)(8f * upFact));
            ++i;
        }
    }
}
