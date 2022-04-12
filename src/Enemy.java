public abstract class Enemy extends GameObject{

    protected Enemy(int textureID, int x, int y, int w, int h){
        super(textureID, x, y, w, h);
    }

    //Main direction of movement: 0 = right, 1 = down, 2 = left, 3 = up
    protected int direction;
    //Slope of movement
    protected double slope;
    //Speed of movement ( between 0.25 and 0.5 pixels/ms )
    protected double speed;
    protected int updateCoolDown;
    protected boolean isDead = false;

    protected abstract void updateMovement();

    public abstract void kill();

}