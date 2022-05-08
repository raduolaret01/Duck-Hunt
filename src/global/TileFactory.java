package global;

import java.util.ArrayList;

public class TileFactory {

    private static ArrayList<String> descriptorList = null;
    public static Tile MakeGameTile(String descriptor, int x, int y, int w, int h){
        //Build list on first call
        if(descriptorList == null){
            descriptorList = new ArrayList<>();
            descriptorList.add("Level1Background");
            descriptorList.add("0");
            descriptorList.add("1");
            descriptorList.add("2");
            descriptorList.add("3");
            descriptorList.add("4");
            descriptorList.add("5");
            descriptorList.add("6");
            descriptorList.add("7");
            descriptorList.add("8");
            descriptorList.add("9");
            descriptorList.add("DuckAlive");
            descriptorList.add("DuckDead");
            descriptorList.add("DiskAlive");
            descriptorList.add("DiskDead");
            descriptorList.add("Cartridge");
            descriptorList.add("Black");
        }
        int index = descriptorList.indexOf(descriptor);
        if(index == -1){
            throw new IllegalStateException("Invalid global.Tile Factory Request!: " + descriptor);
        }
        return new Tile(27 + index, x, y, w, h);
    }
}
