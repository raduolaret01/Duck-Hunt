package global;

import global.HudElements.ProgressTab;
import global.HudElements.RoundCounter;
import global.HudElements.ScoreTab;
import global.HudElements.ShotCounter;

public class Level_1 extends Level{

    private Dog dog = new Dog();

    @Override
    public void init() {

        enemies = new Duck[10];
        for(int i = 0; i < 10; ++i){
            enemies[i] = new Duck();
        }

        HudObjects[0] = new ShotCounter(224,938);
        HudObjects[1] = new ProgressTab(724,938);
        HudObjects[2] = new ScoreTab(1514,938);
        HudObjects[3] = new RoundCounter(224,838);
        HudObjects[4] = new Tile(49,0,716,1920,368);

        for(int i = 1; i < 4; ++i){
            HudObjects[i].update();
        }

    }

    @Override
    public void killAround(int x, int y) {
        for(Enemy ducky : enemies){
            if(!ducky.isDead && Math.abs(ducky.getCenterX() - x) <= 77 && Math.abs(ducky.getCenterY() - y) <= 77){
                ducky.kill();
                ++ducksShot;
            }
        }
        updateMisses();
        updateScore();
    }

    @Override
    public void update() {
        /*for(Enemy enemy : enemies){
            enemy.update();
            if(enemy.isDead &&  enemy.posY > 1080 - enemy.height - groundLevel){ //1080 - duck.Height - GroundLevel
                dog.grabDuckAt(enemy.posX);
                enemy = new Duck();
            }
        }*/
        for (int i =0; i<10; ++i){
            enemies[i].update();
            if(enemies[i].isDead && enemies[i].posY > 1080 - groundLevel){
                dog.grabDuckAt(enemies[i].posX);
                enemies[i] = new Duck();
            }
        }
        dog.update();
        //Update Hud Elements
        HudObjects[1].update();
        HudObjects[3].update();

        ducksShot = 0;
    }

    @Override
    public void draw() {
        for(Enemy enemy : enemies){
            enemy.draw();

        }
        if(dog.isInBackground()){
            dog.draw();
        }
        //Draw Hud Elements
        HudObjects[4].draw(); //grass
        if(!dog.isInBackground()){
            dog.draw();
        }

        for(int i = 0; i < 4; ++i){
            HudObjects[i].draw();
        }

        ducksShot = 0;
    }
}
