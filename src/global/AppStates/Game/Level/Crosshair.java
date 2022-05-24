package global.AppStates.Game.Level;

import global.Cursor;
import global.GraphicObject;
import global.Systems.Renderer;
import global.Systems.Timer;

public class Crosshair extends GraphicObject {

    public Crosshair(){
        super(134,960,540,34,34);
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
    }

    @Override
    public void update() {
        int dT = Timer.getDeltaTime();
        int targetX = Cursor.getInstance().posX + 8, targetY = Cursor.getInstance().posY + 8;
        //speed [0.1, 0.5]
        double speedX = (double)(targetX - posX) / 250d, speedY = (double)(targetY - posY) / 250d;

        if(speedX < 0.1d && speedX > -0.1d){
            speedX = 0.1d * Math.signum(speedX);
        }
        else if(speedX > 0.5d || speedX < -0.5d){
            speedX = 0.5d * Math.signum(speedX);
        }
        if(speedY < 0.1d && speedY > -0.1d){
            speedY = 0.1d * Math.signum(speedY);
        }
        else if(speedY > 0.5d || speedY < -0.5d){
            speedY = 0.5d * Math.signum(speedY);
        }
        posX += speedX * dT;
        //System.out.println("speedx: " + speedX);
        posY += speedY * dT;
        //System.out.println("speedy: " + speedY);
    }
}
