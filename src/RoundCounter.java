public class RoundCounter extends GraphicObject{

    RoundCounter(int x, int y, int w, int h){
        super(47, x, y, w, h);
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
        int tens = 1;
        for(int i = 1; i >= 0; --i) {
            Integer temp = (round / tens) % 10;
            roundTiles[i] = TileFactory.MakeTile(temp.toString(), posX + 38 + i * 16, posY + 4, 16, 16);
            tens *= 10;
        }
    }
}
