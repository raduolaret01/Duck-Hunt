package global;

public abstract class Level {

    protected Enemy[] enemies;
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

    //Information needed for Hud Elements
    /** number of ducks in current round */
    protected static int ducks;
    /** number of ducks hit with a single shot */
    protected static int ducksShot = 0;
    /** current round */
    protected static int round = 1;
    /** Level above which enemies spawn */
    protected static int groundLevel = 240;

    public static int getDucksShot(){
        return ducksShot;
    }

    public static int getRound(){
        return round;
    }

    public static void advanceRound(){
        ++round;
    }

    public static int getGroundLevel(){
        return groundLevel;
    }

}
