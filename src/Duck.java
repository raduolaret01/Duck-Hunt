import java.sql.Time;
import java.util.BitSet;

// TODO: replace hard-coded references to window size with resolution dimensions
// Can "juggle" already dead ducks: Bug or Feature?

public class Duck extends Enemy{

    private int variant = (int) (Math.random() * 3d);
    /**
     * 0 = Move up, 1 = Move side, 2 = Move down, 3 = Shot, 4 = Falling
     */
    private AnimationState[] animStates = new AnimationState[5];
    private AnimationState currentAnimState;
    /**
     * Basically is a "Facing left" flag
     */
    private boolean flip = false;

    private int animUpdateCooldown = 0;

    public Duck(){
        //Starting x position : 480 + rand*960
        //Starting y position : 1018

        super(0, (int)(480 + Math.random() * 960),1018,102,102);

        texID = variant * 9;

        animStates[0] = new AnimationState(texID, 3);
        animStates[1] = new AnimationState(texID + 3, 3);
        animStates[2] = new AnimationState(texID + 6, 1);
        animStates[3] = new AnimationState(texID + 7,1);
        animStates[4] = new AnimationState(texID + 8, 1);

        currentAnimState = animStates[0];
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObjectFlip(this, flip);
    }

    @Override
    protected void updateMovement(){
        updateCoolDown = 0;
        currentAnimState.reset();
        if(isDead){
            currentAnimState = animStates[4];
            direction = 1;
            speed = 0.75d;
            slope = 0d;
            return;
        }
        speed = 0.25d + Math.random() * 0.25d;
        //Quadrants of cartesian coordonate system, centered on duck
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

        slope = Math.random() * 1.73d; // Slope = [0, sqrt(3)]
        //Constantly liniar y = m * x + n movement would have been nice, but can't simulate m->inf cases too well
        //So, based on main direction we change between y = m * x + n and x = m * y  + n



        switch (q) {
            case -1:
                throw new IllegalStateException("Duck movement update failed! No available direction found!");
            case 0:
                if (quadrants.get(1)){ // Left
                    direction = 0;
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = false;
                }
                else if(quadrants.get(3)){ // Up (and left)
                    direction = 3;
                    currentAnimState.reset();
                    currentAnimState = animStates[0];
                    flip = false;
                }
                else { // Escape from bottom left corner
                    direction = 0;
                    slope = -(Math.random() * 1.15d + 0.58d); // Slope = [sqrt(3)/3, sqrt(3)]
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = false;
                }
                break;
            case 1:
                if(quadrants.get(0)){ // Left
                    direction = 0;
                    slope = -slope;
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = false;
                }
                else if(quadrants.get(2)) { // Down (and left)
                    direction = 1;
                    currentAnimState.reset();
                    currentAnimState = animStates[2];
                    flip = false;
                }
                else { // Escape from top left corner
                    direction = 0;
                    slope = Math.random() * 1.15d + 0.58d; // Slope = [-sqrt(3), -sqrt(3)/3]
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = false;
                }
                break;
            case 2:
                if(quadrants.get(1)){ // Down (but to the right this time)
                    direction = 1;
                    slope = -slope;
                    currentAnimState.reset();
                    currentAnimState = animStates[2];
                    flip = true;
                }
                else if(quadrants.get(3)) { // Right (and down)
                    direction = 2;
                    slope = -slope;
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = true;
                }
                else { // Escape from top right corner
                    direction = 2;
                    slope = Math.random() * 1.15d + 0.58d; // Slope = [sqrt(3)/3, sqrt(3)]
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = true;
                }
                break;
            case 3:
                if(quadrants.get(2)){ // Right (and up)
                    direction = 2;
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = true;
                }
                else if(quadrants.get(0)) { // Up (but to the right)
                    direction = 3;
                    slope = -slope;
                    currentAnimState.reset();
                    currentAnimState = animStates[0];
                    flip = true;
                }
                else { // Escape from bottom right corner
                    direction = 2;
                    slope = -(Math.random() * 1.15d + 0.58d); // Slope = [-sqrt(3), -sqrt(3)/3]
                    currentAnimState.reset();
                    currentAnimState = animStates[1];
                    flip = true;
                }
                break;
            default:
                throw new IllegalStateException("Duck movement update failed! Invalid direction found!");
        }
    }

    public void update(){
        int dT = Timer.getDeltaTime();
        animUpdate(dT);
        /**
         * dPos1 = absolute "distance"
         * dPos2 = not absolute ( has sign of slope )
         */
        int dPos1 = (int)(speed * dT), dPos2 = (int)(dPos1 * slope);
        switch (direction){
            case -1:
                updateCoolDown += Timer.getDeltaTime();
                break;
            case 0: // Left
                posX += dPos1; // x increases
                posY += dPos2; // y += slope * dx
                break;
            case 1: // Down
                posY += dPos1; // y increases ( top left is 0,0 )
                posX += dPos2; // x += slope * dy
                break;
            case 2: // Right
                posX -= dPos1; // x decreases
                posY += dPos2; // y += slope * dx
                break;
            case 3: // Up
                posY -= dPos1; // y decreases
                posX += dPos2; // x += slope * dy
                break;
            default:
                throw new IllegalStateException("Invalid duck movement direction!");
        }
        if(isDead){
            if(updateCoolDown >= 1000){
                updateMovement();
            }
            return;
        }

        if(posX < 60 || posX > 1758 || posY < 60 || posY > 918 ){
            updateMovement();
        }
        int chance = (int)(Math.random() * 1000);
        updateCoolDown += Timer.getDeltaTime();
        if(updateCoolDown < 250){
            return;
        }
        if(chance <= 5){
            updateMovement();
        }
    }

    private void animUpdate(int dT){
        animUpdateCooldown += dT;
        if(animUpdateCooldown > 160){
            texID = currentAnimState.update();
            if(currentAnimState == animStates[4]){
                flip = !flip;
            }
            animUpdateCooldown = 0;
        }
    }

    public void kill(){
        isDead = true;
        direction = -1;
        speed = 0.5f;
        currentAnimState = animStates[3];
        updateCoolDown = 0;
        animUpdate(1000); // Force frame update
    }

}
