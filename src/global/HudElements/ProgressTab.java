package global.HudElements;


import global.*;

public class ProgressTab extends GraphicObject {

    public ProgressTab(int x, int y, int w, int h){
        super(45, x, y, w, h);
    }

    //Default size (for 1080p): 472 * 84
    public ProgressTab(int x, int y){
        super(45, x, y, 472, 84);
    }
    private int maxDucks = 10;
    private int hitDucks = 0;
    private float timeElapsed = 0;
    private Tile[] duckTiles = new Tile[maxDucks];
    private Tile timerTile = TileFactory.MakeGameTile("Black", 0,0,8,8);

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
        timerTile.drawAtDestination(posX + 468 - (int)(8f * timeElapsed),posY + 52, (int)(8f * timeElapsed), 20);

        for(Tile duck : duckTiles){
            duck.draw();
        }
    }

    @Override
    public void update() {
        timeElapsed += ((float) Timer.getDeltaTime()) / 1000f;
        //timer reset (game over not implemented)
        if(timeElapsed >= 40f){
            timeElapsed -= 40f;
        }
        hitDucks += Game.currentLevel.getDucksShot();
        if(hitDucks >= maxDucks){
            hitDucks = 0;
            Game.currentLevel.advanceRound();
        }

        int i = 0;
        while(i < hitDucks){
            duckTiles[i] = TileFactory.MakeGameTile("DuckDead",posX + 140 + i * 32,posY + 12,32,32);
            ++i;
        }
        while(i < maxDucks){
            duckTiles[i] = TileFactory.MakeGameTile("DuckAlive",posX + 140 + i * 32,posY + 12,32,32);
            ++i;
        }
    }
}
