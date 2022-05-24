package global;

public class Dog extends GraphicObject{

    private double targetPos = 460d + Math.random() * 160d;
    private double speed = 0.3d + Math.random() * 0.1d;
    private int updateCooldown = 0, animUpdateCooldown = 0;
    /** 0 = Game start walk
     * 1 = Game start sniff
     * 2 = Game start pog
     * 3 = Game start jump
     * 4 = Duck pick-up wait
     * 5 = Duck pick-up rise
     * 6 = Duck pick-up fall
     * 7 = Game end laugh
     */
    private int state = 0;
    private int heldDucks = 0;
    private int duckPickupPos = -1;
    private AnimationState[] animStates = new AnimationState[8];
    private AnimationState currentAnimState;

    public Dog(){
        super(48,-230,660,220,200);
        System.out.println(this.posY);
        //Anim states init
        animStates[0] = new AnimationState(texID, 4); //walk
        animStates[1] = new AnimationState(texID + 4, 2); //sniff
        animStates[2] = new AnimationState(texID + 6, 1); //find
        animStates[3] = new AnimationState(texID + 7,2); //jump
        animStates[4] = new AnimationState(texID + 9, 1); //1duck
        animStates[5] = new AnimationState(texID + 10, 1); //2duck
        animStates[6] = new AnimationState(texID + 11, 1); //3duck
        animStates[7] = new AnimationState(texID + 12, 2); //laugh

        currentAnimState = animStates[0];
    }

    @Override
    public void draw() {
        Renderer.getInstance().DrawObject(this);
    }

    @Override
    public void update() {
        int dT = Timer.getDeltaTime();
        animUpdate(dT);
        switch (state) {
            case 0:
                if(Math.random() < 0.01d){
                    state = 1;
                    currentAnimState = animStates[1];
                }
                else if (posX != targetPos) {
                    posX += speed * dT;
                }
                else {
                    state = 1;
                    currentAnimState = animStates[1];
                }
                break;
            case 1:
                updateCooldown += dT;
                if(updateCooldown >= 1200){
                    if(posX >= targetPos) {
                        state = 2;
                        currentAnimState = animStates[2];
                    }
                    else {
                        state = 0;
                        currentAnimState = animStates[0];
                    }
                    updateCooldown = 0;
                }
                break;
            case 2:
                updateCooldown += dT;
                if(updateCooldown >= 400){
                    state = 3;
                    posY -=60;
                    currentAnimState = animStates[3];
                    updateCooldown = 0;
                }
                break;
            case 3:
                updateCooldown += dT;
                if(updateCooldown >= 450){
                    state = 4;
                    posY +=60;
                    currentAnimState = animStates[4];
                    updateCooldown = 0;
                }
                break;
            case 4:
                this.width = this.height = 0;
                if(Level.GameOver()){
                    currentAnimState = animStates[7];
                    state = 7;
                    posX = 902;
                    width = 116;
                    height = 160;
                    targetPos = 760 - this.height;
                    speed = this.height / 500d;
                    updateCooldown = 0;
                    break;
                }
                updateCooldown += dT;
                if(updateCooldown >= 500 && heldDucks != 0){
                    currentAnimState = animStates[3 + heldDucks];
                    state = 5;
                    posX = duckPickupPos;
                    this.width = 224;
                    this.height = 232; //different size frames
                    targetPos = 760 - this.height;
                    speed = this.height / 500d;
                    updateCooldown = 0;
                    heldDucks = 0;
                }
                break;
            case 5:
                if(posY > targetPos){
                    posY -= speed * dT;
                }

                updateCooldown += dT;
                if(updateCooldown >= 2000){
                    state = 6;
                    targetPos = 830;
                    updateCooldown = 0;
                }
                break;
            case 6:
                if(posY < targetPos){
                    posY += speed * dT;
                }
                else {
                    state = 4;
                }
                break;
            case 7:
                if(posY > targetPos){
                    posY -= speed * dT;
                }
                break;
        }

    }

    public void grabDuckAt(int pos){
        heldDucks ++;
        if(heldDucks > 3){
            heldDucks = 3;
        }
        duckPickupPos = pos - 44;
        System.out.println("Duck picked up!");
    }

    public boolean isInBackground(){
        return state > 3;
    }

    private void animUpdate(int dT){
        animUpdateCooldown += dT;
        if(animUpdateCooldown > 160){
            texID = currentAnimState.updateLoop();
            animUpdateCooldown = 0;
        }
    }


}
