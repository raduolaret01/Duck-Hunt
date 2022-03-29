public class Timer {
    private static int lastTime = 0;
    private static int deltaTime = 0;

    public static void startTime(){
        lastTime = (int)(System.nanoTime()/1000000);
    }

    public static void setDeltaTime(){
        int currentTime = (int)(System.nanoTime()/1000000);
        deltaTime = currentTime - lastTime;
        lastTime = currentTime;
    }

    public static int getDeltaTime(){
        return deltaTime;
    }
}
