
public class AnimationState {
    private int baseFrame, numberOfFrames, currentFrameOffset = 0, increment = 1;

    AnimationState(int baseFrame, int numberOfFrames){
        this.baseFrame = baseFrame;
        this.numberOfFrames = numberOfFrames;
    }

    public int update(){
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

    public void reset(){
        currentFrameOffset = 0;
        increment = 1;
    }
}
