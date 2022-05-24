package global.AppStates.Game.Level;

import global.Systems.Renderer;
import global.Systems.Timer;

public class Disk extends Enemy{

    private double posZ = 0;
    private double dSlope, dZ;

    public Disk(){
        super(133, 680 + (int) (Math.random() * 560),960, 96, 64);
        if(posX > 960){
            direction = 2;
        }
        else direction = 0;
        dSlope = 0.0002d + Math.random() * 0.00005d;
        slope = 1.732d + Math.random() * 2d; //3.7320 - 1.732
        //slope/=2;
        dZ = 0.02d;
        speed = (0.08d + Math.random() * 0.005d) * Level.getDifficulty();
    }

    @Override
    protected void updateMovement() {
        int dT = Timer.getDeltaTime();
        slope -= dT * dSlope;
        posZ += dT * dZ;
        this.width = (int) (96d * (100d - posZ)/100d);
        this.height = (int) (64d * (100d - posZ)/100d);
    }

    @Override
    public void kill() {
        posZ = 100;
    }

    @Override
    public boolean isOffscreen() {
        return posZ >= 100;
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
    }

    @Override
    public void update() {
        int dT = Timer.getDeltaTime();
        int dPos1 = (int)(speed * dT), dPos2 = (int)(dPos1 * -slope);
        //System.out.println(slope);
        switch (direction){
            case 0: // Left
                posX += dPos1; // x increases
                posY += dPos2; // y += slope * dx
                break;
            case 2: // Right
                posX -= dPos1; // x decreases
                posY += dPos2; // y += slope * dx
                break;
            default:
                throw new IllegalStateException("Invalid Disk movement direction: " + direction);
        }
        updateMovement();
    }
}
