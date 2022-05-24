package global.Systems.DataStructures;

public class AnimationState {
    private final int baseFrame;
    private final int numberOfFrames;
    private int currentFrameOffset = 0;
    private int increment = 1;

    public AnimationState(int baseFrame, int numberOfFrames){
        this.baseFrame = baseFrame;
        this.numberOfFrames = numberOfFrames;
    }

    public int updateBounce(){
        if(numberOfFrames == 1){
            return baseFrame;
        }
        if(currentFrameOffset == numberOfFrames - 1) {
            increment = -1;
        }
        else if(currentFrameOffset == 0){
            increment = 1;
        }
        currentFrameOffset += increment;
        return baseFrame + currentFrameOffset;
    }

    public int updateLoop(){
        if(numberOfFrames == 1){
            return baseFrame;
        }
        if(currentFrameOffset == numberOfFrames - 1) {
            currentFrameOffset = -1;
        }
        currentFrameOffset += increment;
        return baseFrame + currentFrameOffset;
    }

    public void reset(){
        currentFrameOffset = 0;
        increment = 1;
    }
}
