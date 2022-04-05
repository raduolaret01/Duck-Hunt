import java.sql.Time;
import java.util.BitSet;

// TODO: replace hard-coded references to window size with relative window dimensions
// Can "juggle" already dead ducks: Bug or Feature?

public class Duck extends Enemy{

    public Duck(){
        //Starting x position : 480 + rand*960
        //Starting y position : 1018
        super(0, (int)(480 + Math.random() * 960),1018,102,102);
    }

    protected void updateMovement(){
        updateCoolDown = 0;
        if(isDead){
            texID = 2;
            direction = 1;
            speed = 0.75d;
            slope = 0d;
            return;
        }
        speed = 0.25d + Math.random() * 0.25d;
        //Quadrants of cartesian coordonate system
        BitSet quadrants = new BitSet(4);
        quadrants.set(0,4);
        //If the duck is too close to the edge of the screen, it will select an opposite direction
        if(posX < 100){ // 100 pixel margin in case of random update sounds good
            quadrants.clear(2);
            quadrants.clear(3);
        }
        else if(posX > 1718) { // 1920 - width - 100
            quadrants.clear(0);
            quadrants.clear(1);
        }
        if(posY < 100){
            quadrants.clear(0);
            quadrants.clear(3);
        }
        else if(posY > 878) { // 1080 - height - 100
            quadrants.clear(1);
            quadrants.clear(2);
        }
        int c = quadrants.cardinality(), q = quadrants.nextSetBit(0);
        if(c > 1){
            q += (int)(Math.random() * c);
            if(!quadrants.get(q)){
                q = quadrants.nextSetBit(q);
            }
        }

        slope = Math.random() * 1.73f; // Slope = [0, sqrt(3)]
        //Constantly liniar y = m * x + n movement would have been nice, but can't simulate m->inf cases too well
        //So, based on main direction we change between y = m * x + n and x = m * y  + n

        switch (q) {
            case -1:
                throw new IllegalStateException("Duck movement update failed! No available direction found!");
            case 0:
                if (quadrants.get(1)){
                    direction = 0;
                }
                else if(quadrants.get(3)){
                    direction = 3;
                }
                else { //Escape from corner
                    direction = 0;
                    slope = (int)(Math.random() * 1.15f + 0.58f); // Slope = [sqrt(3)/3, sqrt(3)]
                }
                break;
            case 1:
                if(quadrants.get(0)){
                    direction = 0;
                    slope = -slope;
                }
                else if(quadrants.get(2)) {
                    direction = 1;
                }
                else {
                    direction = 0;
                    slope = -(int)(Math.random() * 1.15f + 0.58f); // Slope = [-sqrt(3), -sqrt(3)/3]
                }
                break;
            case 2:
                if(quadrants.get(1)){
                    direction = 1;
                    slope = -slope;
                }
                else if(quadrants.get(3)) {
                    direction = 2;
                }
                else {
                    direction = 2;
                    slope = (int)(Math.random() * 1.15f + 0.58f); // Slope = [-sqrt(3), -sqrt(3)/3]
                }
                break;
            case 3:
                if(quadrants.get(2)){
                    direction = 2;
                    slope = -slope;
                }
                else if(quadrants.get(0)) {
                    direction = 3;
                }
                else {
                    direction = 2;
                    slope = -(int)(Math.random() * 1.15f + 0.58f); // Slope = [sqrt(3)/3, sqrt(3)]
                }
                break;
            default:
                throw new IllegalStateException("Duck movement update failed! Invalid direction found!");
        }
    }

    public void update(){
        int dT = Timer.getDeltaTime(), dPos1 = (int)(speed * dT), dPos2 = (int)(dPos1 * slope);
        switch (direction){
            case -1:
                updateCoolDown += Timer.getDeltaTime();
                break;
            case 0:
                posX += dPos1;
                posY += dPos2;
                break;
            case 1:
                posX += dPos2;
                posY += dPos1;
                break;
            case 2:
                posX -= dPos1;
                posY += dPos2;
                break;
            case 3:
                posX += dPos2;
                posY -= dPos1;
                break;
            default:
                throw new IllegalStateException("Invalid duck movement direction!");
        }
        //System.out.println(posX + " " + posY + " " + direction + " " + slope);
        if(isDead){
            if(updateCoolDown >= 1000){
                updateMovement();
            }
            return;
        }

        if(posX < 60 || posX > 1758 || posY < 60 || posY > 918 ){
            updateMovement();
        }
        int chance = (int)(Math.random() * 100);
        updateCoolDown += Timer.getDeltaTime();
        if(updateCoolDown < 250){
            return;
        }
        if(chance <= 2){
            updateMovement();
        }
    }

    public void kill(){
        isDead = true;
        direction = -1;
        speed = 0.5f;
        texID = 1;
    }

}
