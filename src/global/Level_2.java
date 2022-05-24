package global;

import global.HudElements.ProgressTab;
import global.HudElements.RoundCounter;
import global.HudElements.ScoreTab;
import global.HudElements.ShotCounter;

import java.util.ArrayList;

public class Level_2 extends Level{
    private Dog dog = new Dog();

    @Override
    public void init() {

        state = 0;
        round = 0;
        ducks = 3;

        enemies = new ArrayList<Enemy>(10);
        for(int i =  0; i < 10; ++i){
            enemies.add(null);
        }

        Duck.setRunAway(false);

        HudObjects[0] = new ShotCounter(224,938);
        HudObjects[1] = new ProgressTab(724,938, ducks);
        HudObjects[2] = new ScoreTab(1514,938);
        HudObjects[3] = new RoundCounter(224,838);
        HudObjects[4] = new Tile(65,0,716,1920,368);

        for(int i = 1; i < 4; ++i){
            HudObjects[i].update();
        }

    }

    @Override
    public void killAround(int x, int y) {
        for(Enemy ducky : enemies){
            if(ducky!=null && !ducky.isDead && Math.abs(ducky.getCenterX() - x) <= 77 && Math.abs(ducky.getCenterY() - y) <= 77){
                ducky.kill();
                ++ducksShot;
            }
        }
        updateMisses();
        updateScore();
    }

    @Override
    public void update() {
        switch (state) {
            case 0:
                dog.update();
                if(dog.isInBackground()) {
                    for (int i = 0; i < ducks; ++i) {
                        enemies.set(i, new Duck());
                    }
                    state = 1;
                }
                break;
            case 1:
                for (int i = 0; i < ducks; ++i) {
                    if (enemies.get(i) != null) {
                        enemies.get(i).update();
                        if (enemies.get(i).isDead && enemies.get(i).isOffscreen()) {
                            dog.grabDuckAt(enemies.get(i).posX);
                            enemies.set(i, null);
                        }
                    }
                }
                dog.update();
                //Update Hud Elements
                HudObjects[1].update();
                HudObjects[3].update();

                ducksShot = 0;

                state = 2;
                for (int i = 0; i < ducks; ++i) {
                    if (enemies.get(i) != null) {
                        state = 1;
                    }
                }
                break;
            case 2:
                HudObjects[1].update();
                HudObjects[3].update();
                dog.update();
                updateCooldown += Timer.getDeltaTime();
                if(updateCooldown >= 2000){
                    advanceRound();
                    respawnDucks();
                    state = 1;
                    updateCooldown = 0;
                }
            case 3:
                dog.update();
                updateCooldown += Timer.getDeltaTime();
                for (int i = 0; i < ducks; ++i) {
                    if (enemies.get(i) != null) {
                        enemies.get(i).update();
                        if (enemies.get(i).isOffscreen()) {
                            enemies.set(i, null);
                        }
                    }
                }
                if(updateCooldown >= 4000){
                    Game.setExitFlag(true);
                }
        }

        ducksShot = 0;
    }
    @Override
    public void draw() {
        for(Enemy enemy : enemies){
            if(enemy != null) {
                enemy.draw();
            }
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

    }

    private void respawnDucks(){
        for(int i = 0; i < ducks; ++i){
            enemies.set(i, new Duck());
        }
        for(int i = ducks; i < 10; ++i){
            enemies.set(i, null);
        }
    }
}
