package global.HudElements;

import global.*;

public class ShotCounter extends GraphicObject {

    public ShotCounter(int x, int y, int w, int h){
        super(46, x, y, w, h);
        for(int i = 0; i < 3; ++i){
            shotsTiles[i] = TileFactory.MakeTile("Cartridge",posX + 38 - 16*i,posY + 4,16,16);
        }
    }

    public ShotCounter(int x, int y){
        super(46,x,y,(int) (30f * (Renderer.getInstance().getUpscaleFactor())), (int) (21f * Renderer.getInstance().getUpscaleFactor()));
        float upFact = Renderer.getInstance().getUpscaleFactor();
        for(int i = 0; i < 3; ++i){
            shotsTiles[i] = TileFactory.MakeTile("Cartridge",posX + (int)(19f * upFact) - (int)(8f * upFact) * i,posY + (int)(2f * upFact),(int)(8f * upFact),(int)(8f * upFact));
        }
    }

    private Tile[] shotsTiles = new Tile[3];
    private int misses;

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
        for(Tile shot : shotsTiles){
            shot.draw();
        }
    }

    @Override
    public void update() {
        if(Game.getDucksShot() == 0){
            misses++;
        }
        if(misses > 3){
            misses = 0;
        }
        float upFact = Renderer.getInstance().getUpscaleFactor();
        int i = 0;
        while(i < misses){
            shotsTiles[i] = TileFactory.MakeTile("Black",posX + (int)(19f * upFact) - (int)(8f * upFact) * i,posY + (int)(2f * upFact),(int)(8f * upFact),(int)(8f * upFact));
            ++i;
        }
        while(i < 3){
            shotsTiles[i] = TileFactory.MakeTile("Cartridge",posX + (int)(19f * upFact) - (int)(8f * upFact) * i,posY + (int)(2f * upFact),(int)(8f * upFact),(int)(8f * upFact));
            ++i;
        }
    }
}
