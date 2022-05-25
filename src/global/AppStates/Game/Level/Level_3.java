package global.AppStates.Game.Level;

import global.AppStates.Game.Game;
import global.AppStates.Game.Level.HudElements.ProgressTab;
import global.AppStates.Game.Level.HudElements.RoundCounter;
import global.AppStates.Game.Level.HudElements.ScoreTab;
import global.AppStates.Game.Level.HudElements.ShotCounter;
import global.Systems.MyException;
import global.Systems.Timer;
import global.Tile;

import java.util.ArrayList;

public class Level_3 extends Level{

    private int disksPerVolley = 1;

    @Override
    public void init() throws MyException {

        state = 0;
        round = 0;
        ducks = 10;
        disksPerVolley = 1;

        enemies = new ArrayList<Enemy>(4);
        for(int i =  0; i < 4; ++i){
            enemies.add(null);
        }

        HudObjects[0] = new ShotCounter(224,938);
        HudObjects[1] = new ProgressTab(724,938, ducks, "Disk");
        HudObjects[2] = new ScoreTab(1514,938);
        HudObjects[3] = new RoundCounter(224,838);
        HudObjects[4] = new Tile(66,0,872,1920,208);

        for(int i = 1; i < 4; ++i){
            HudObjects[i].update();
        }

        refillDisks();
    }

    @Override
    public void killAround(int x, int y) throws MyException {
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
    public void update() throws MyException {
        switch (state){
            case 0:
                for(int i = 0; i < disksPerVolley; ++i){
                    if(enemies.get(i)!=null) {
                        enemies.get(i).update();
                        //System.out.println("Disk " + i + " updated : " + enemies.get(i).posX + " " + enemies.get(i).posY);
                        if(enemies.get(i).isOffscreen()){
                            enemies.set(i, null);
                        }
                    }
                }
                //Update Hud Elements
                HudObjects[1].update();
                HudObjects[3].update();

                ducksShot = 0;

                state = 1;
                for (int i = 0; i < disksPerVolley; ++i) {
                    if (enemies.get(i) != null) {
                        state = 0;
                    }
                }
                break;
            case 1:
                HudObjects[3].update();
                HudObjects[1].update();
                updateCooldown += Timer.getDeltaTime();
                if(updateCooldown >= 1000 - Math.min(round,9) * 100){
                    state = 0;
                    advanceRound();
                    disksPerVolley = Math.min(Math.max(round / 3, 1), 4);
                    refillDisks();
                    updateCooldown = 0;
                }
                break;
            case 3:
                for(int i = 0; i < disksPerVolley; ++i){
                    if(enemies.get(i)!=null) {
                        enemies.get(i).update();
                        if(enemies.get(i).isOffscreen()){
                            enemies.set(i, null);
                        }
                    }
                }
                updateCooldown += Timer.getDeltaTime();
                if(updateCooldown >= 3000){
                    Game.setExitFlag(true);
                }
                break;
            default:
                throw new MyException("Invalid state value at Level_3: " + state);
        }
    }

    @Override
    public void draw() {

        for(Enemy enemy : enemies){
            if(enemy != null) {
                enemy.draw();
            }
        }
        //Draw Hud Elements
        HudObjects[4].draw(); //grass

        for(int i = 0; i < 4; ++i){
            HudObjects[i].draw();
        }
    }

    private void refillDisks(){
        for(int i = 0; i < disksPerVolley; ++i){
            enemies.set(i, new Disk());
        }
        for(int i = disksPerVolley; i < 4; ++i){
            enemies.set(i, null);
        }
    }
}
