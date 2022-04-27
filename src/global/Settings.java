package global;

public class Settings {
    private int resolutionW, resolutionH, volume;

    public int getResolutionW(){
        return resolutionW;
    }

    public int getResolutionH() {
        return resolutionH;
    }

    public int getVolume() {
        return volume;
    }



    public Settings(int w , int h , int vol){
        resolutionW = w;
        resolutionH = h;
        volume = vol;
    }
}
