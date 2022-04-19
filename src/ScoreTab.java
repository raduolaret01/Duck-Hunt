public class ScoreTab extends GraphicObject{

    ScoreTab(int x, int y, int w, int h){
        super(22, x, y, w, h);
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
            scoreTiles[i] = TileFactory.MakeTile(temp.toString(),posX + 6 + i * 16, posY + 6,16,16);
            tens *= 10;
        }
    }

}
