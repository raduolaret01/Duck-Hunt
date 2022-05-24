package global.AppStates.Game.Level;

import global.GraphicObject;

import java.util.ArrayList;

public abstract class Level {

    protected ArrayList<Enemy> enemies;
    /** 0 = Shots, 1 = Round Progress, 2 = Score, 3 = Round number, 4 = GrassTile(for now) */
    protected GraphicObject[] HudObjects = new GraphicObject[5];

    public abstract void init();

    /**Checks for 'enemies' around cursor (level 1) or target (levels 2 and 3) and calls their "kill" function*/
    public abstract void killAround(int x, int y);

    public abstract void update();

    public abstract void draw();

    public void updateScore(){
        HudObjects[2].update();
    }
    public void updateMisses(){
        HudObjects[0].update();
    }

    /** 0 = Level start, 1 = During round, 2 = Between rounds, 3 = Game Over */
    protected static int state = 0;
    protected static int updateCooldown = 0;

    //Information needed for Hud Elements
    /** number of ducks in current round */
    protected static int ducks;
    /** number of ducks hit with a single shot */
    protected static int ducksShot = 0;
    /** current round */
    protected static int round = 1;

    public static int getDucks(){
        return ducks;
    }

    public static int getDucksShot(){
        return ducksShot;
    }

    public static int getRound(){
        return round;
    }

    public static double getDifficulty(){
        return 1d + (double)round / 100d;
    }

    public static void setGameOver(){
        state = 3;
        Duck.setRunAway(true);
        updateCooldown = 0;
    }

    public static boolean GameOver(){
        return state == 3;
    }

    public static boolean RoundTransition(){
        return state == 2;
    }

    public static void advanceRound(){
        ++round;
        if(ducks < 10){
            ++ducks;
        }
    }


}
