package global;

import java.util.Set;

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

    public void setResolution(int w, int h){
        resolutionW = w;
        resolutionH = h;
    }

    public void lowerVolume(int amount){
        if(volume > 0) {
            volume -= amount;
            if (volume < 0){
                volume = 0;
            }
        }
    }

    public void increaseVolume(int amount){
        if(volume < 100) {
            volume += amount;
            if(volume > 100){
                volume = 100;
            }
        }
    }

    public Settings(int w , int h , int vol){
        resolutionW = w;
        resolutionH = h;
        volume = vol;
    }

    public Settings(Settings set){
        resolutionW = set.resolutionW;
        resolutionH = set.resolutionH;
        volume = set.volume;
    }
}
