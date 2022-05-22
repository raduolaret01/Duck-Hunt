package global;

import java.util.ArrayList;

public class TileFactory {

    private static ArrayList<String> tileDescriptorList = null;
    public static Tile MakeGameTile(String descriptor, int x, int y, int w, int h){
        //Build list on first call
        if(tileDescriptorList == null){
            tileDescriptorList = new ArrayList<>();
            tileDescriptorList.add("0");
            tileDescriptorList.add("1");
            tileDescriptorList.add("2");
            tileDescriptorList.add("3");
            tileDescriptorList.add("4");
            tileDescriptorList.add("5");
            tileDescriptorList.add("6");
            tileDescriptorList.add("7");
            tileDescriptorList.add("8");
            tileDescriptorList.add("9");
            tileDescriptorList.add("DuckAlive");
            tileDescriptorList.add("DuckDead");
            tileDescriptorList.add("DiskAlive");
            tileDescriptorList.add("DiskDead");
            tileDescriptorList.add("Cartridge");
            tileDescriptorList.add("Black");
        }
        int index = tileDescriptorList.indexOf(descriptor);
        if(index == -1){
            throw new IllegalArgumentException("Invalid TileFactory Tile Request!: " + descriptor);
        }
        return new Tile(27 + index, x, y, w, h);
    }

    public static Tile MakeBGTile(int level){
        if(level < 1 || level > 3){
            throw new IllegalArgumentException("Invalid TileFactory BG Request!: Level " + level);
        }
        return new Tile(61 + level, 0, 0, 1920, 1080);
    }
/** Returns a tile with the specified character from the menu font.
 * Currently a-z and 0-9 only. */
    public static Tile MakeSBTile(char c, int x, int y, int w, int h){
        if(c >='a' && c <= 'z'){
            return new Tile (c-6, x, y, w, h);
        }
        else if(c >= '0' && c <= '9'){
            return new Tile(c + 69, x, y, w, h);
        }
        else{
            throw new IllegalArgumentException("Invalid TileFactory SBTile Request!: " + c);
        }
    }
    //Might actually merge the Button and Tile classes.
    private static ArrayList<String> buttonDescriptorList = null;

    public static Button MakeButton(String descriptor, int x, int y, int w, int h){
        if(buttonDescriptorList == null){
            buttonDescriptorList = new ArrayList<>(18);
            buttonDescriptorList.add("LeftArrow");
            buttonDescriptorList.add("RightArrow");
            buttonDescriptorList.add("Resolution1");
            buttonDescriptorList.add("Resolution2");
            buttonDescriptorList.add("Resolution3");
            buttonDescriptorList.add("ScoreDelete");
            buttonDescriptorList.add("MainMenu");
            buttonDescriptorList.add("Retry");
            buttonDescriptorList.add("Blank");
            buttonDescriptorList.add("PlayGame");
            buttonDescriptorList.add("Options");
            buttonDescriptorList.add("Leaderboard");
            buttonDescriptorList.add("Level1");
            buttonDescriptorList.add("Level2");
            buttonDescriptorList.add("Level3");
            buttonDescriptorList.add("Yes");
            buttonDescriptorList.add("No");
            buttonDescriptorList.add("Apply");
            buttonDescriptorList.add("Back");
        }
        int index = buttonDescriptorList.indexOf(descriptor);
        if(index == -1){
            throw new IllegalArgumentException("Invalid TileFactory Tile Request!: " + descriptor);
        }
        return new Button(73 + index, x, y, w, h);
    }
}
