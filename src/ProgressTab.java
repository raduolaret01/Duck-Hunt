public class ProgressTab extends GraphicObject{

    ProgressTab(int x, int y, int w, int h){
        super(45, x, y, w, h);
    }

    private int maxDucks = 10;
    private int hitDucks = 0;
    private float timeElapsed = 0;
    private Tile[] duckTiles = new Tile[maxDucks];
    private Tile timerTile = TileFactory.MakeTile("Black", 0,0,8,8);

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
        timerTile.drawAtDestination(posX + 228 - 4*(int)timeElapsed,posY + 26,4*(int)timeElapsed,10);
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
        int i = 0;
        while(i < hitDucks){
            duckTiles[i] = TileFactory.MakeTile("DuckDead",posX + 70 + 16*i,posY + 6,16,16);
            ++i;
        }
        while(i < maxDucks){
            duckTiles[i] = TileFactory.MakeTile("DuckAlive",posX + 70 + 16*i,posY + 6,16,16);
            ++i;
        }
    }
}
