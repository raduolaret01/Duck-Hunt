package global.AppStates.Game.Level.HudElements;


import global.*;
import global.AppStates.Game.Level.Level;
import global.Systems.Renderer;
import global.Systems.TileFactory;
import global.Systems.Timer;

public class ProgressTab extends GraphicObject {

    //Default size (for 1080p): 472 * 84
    public ProgressTab(int x, int y, int ducks, String type){
        super(44, x, y, 472, 84);
        maxDucks = ducks;
        duckTiles = new Tile[10];
        enemyType = type;
    }
    private String enemyType;
    private int maxDucks;
    private int hitDucks = 0;
    private float timeElapsed = 0;
    private Tile[] duckTiles;
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
        if(Level.RoundTransition()){
            timeElapsed = 0f;
            hitDucks = 0;
        }
        //timer reset (game over not implemented)
        if(timeElapsed >= 40f){
            Level.setGameOver();
        }
        hitDucks += Level.getDucksShot();
        if(hitDucks > 10){
            hitDucks = 10;
        }
        maxDucks = Level.getDucks();

        //This block (and HUD elements in general) can be greatly optimized
        int i = 0;
        while(i < hitDucks){
            duckTiles[i] = TileFactory.MakeGameTile(enemyType + "Dead",posX + 140 + i * 32,posY + 12,32,32);
            ++i;
        }
        while(i < maxDucks){
            duckTiles[i] = TileFactory.MakeGameTile(enemyType + "Alive",posX + 140 + i * 32,posY + 12,32,32);
            ++i;
        }
        while(i < 10){
            duckTiles[i] = TileFactory.MakeGameTile("Black",posX + 140 + i * 32,posY + 12,32,32);
            ++i;
        }
    }
}
