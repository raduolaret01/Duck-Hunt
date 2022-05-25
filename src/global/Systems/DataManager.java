package global.Systems;

import global.Application;
import global.Systems.DataStructures.ScoreEntry;
import global.Systems.DataStructures.Texture;

import java.sql.*;
import java.util.ArrayList;


public class DataManager {

    private static int numberOfScoreEntries = 0;
    private static ArrayList<ScoreEntry> scoreList = null;
    private static int lastCheckIndex = 0;

    public static int getNumberOfScoreEntries(){
        return numberOfScoreEntries;
    }

    public static  void Init(){
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();
            scoreList = new ArrayList<>(11);
            ResultSet select = s.executeQuery("SELECT * FROM TopScores;");
            numberOfScoreEntries = 0;
            while(select.next()){
                scoreList.add(new ScoreEntry(select.getString(1),select.getInt(2)));
                ++numberOfScoreEntries;
            }
            select.close();
            s.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(1);
        }
    }

    private static void sortList(){
        for(int i = 0; i < numberOfScoreEntries - 1; ++i){
            for(int j = i + 1; j < numberOfScoreEntries; ++j){
                if(scoreList.get(i).compareTo(scoreList.get(j)) < 0){
                    ScoreEntry temp = scoreList.get(i);
                    scoreList.set(i,scoreList.get(j));
                    scoreList.set(j,temp);
                }
            }
        }
    }

    public static Texture[] LoadTextureInfo() {
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();
            ResultSet select = s.executeQuery("SELECT * FROM TextureInfo;");
            //.....I hate this
            int rowCount = 0;
            while(select.next()){
                ++rowCount;
            }
            Texture[] tempAtlas = new Texture[rowCount];
            select.close();
            //
            select = s.executeQuery("SELECT * FROM TextureInfo;");
            while(select.next()){
                int id = select.getInt(1), x = select.getInt(2), y = select.getInt(3), w = select.getInt(4), h = select.getInt(5);
                tempAtlas[id] = new Texture(x,y,w,h);
            }
            select.close();
            s.close();
            c.close();
            return tempAtlas;
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + " at LoadTextureInfo :" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static boolean checkScore(int score){
        if(numberOfScoreEntries < 10) {
            return true;
        }
        else {
            for(lastCheckIndex = numberOfScoreEntries - 1; lastCheckIndex >= 0 && score < scoreList.get(lastCheckIndex).score; --lastCheckIndex);
            return lastCheckIndex >= 0;
        }
    }

    public static void InsertNewScore(ScoreEntry entry){
        scoreList.add(entry);
        ++numberOfScoreEntries;
        sortList();
        if(numberOfScoreEntries > 10){
            scoreList.remove(10);
            --numberOfScoreEntries;
        }
    }

    public static ArrayList<ScoreEntry> getScores(){
        return scoreList;
    }

    public static void DeleteScores(){
        scoreList = new ArrayList<>(11);
    }

    public static void SaveScores(){
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();
            s.execute("DELETE FROM TopScores");
            for(int i = 0; i < numberOfScoreEntries; ++i){
                s.execute("INSERT INTO TopScores (Name, Score) " +
                        "VALUES ('" + scoreList.get(i).name + "', '" + scoreList.get(i).score + "');");
                System.out.println("Inserted " + scoreList.get(i).name + ", " + scoreList.get(i).score);
            }
            c.commit();
            s.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + " at SaveScores :" + e.getMessage());
            System.exit(1);
        }
    }

    public static Settings LoadSetttings(){
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();
            ResultSet select = s.executeQuery("SELECT * FROM Settings");
            if(!select.next()){
                System.out.println("Failed to load settings!");
                select.close();
                s.close();
                c.close();
                return new Settings(1920,1080,100);
            }
            int resW = select.getInt(1), resH = select.getInt(2), vol = select.getInt(3);
            if(((resW == 1920 && resH == 1080) || (resW == 1280 && resH == 720) || (resW == 848 && resH == 480))&& (vol >=0 && vol <= 100)){
                select.close();
                s.close();
                c.close();
                return new Settings(resW, resH, vol);
            }
            select.close();
            s.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + " at GetSettings:" + e.getMessage());
            System.exit(1);
        }
        return new Settings(1920,1080,100);
    }

    public static void SaveSettings(){
        Settings temp = Application.getSettings();
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();
            s.execute("DELETE FROM Settings");
                s.execute("INSERT INTO Settings " +
                        "VALUES ('" + temp.getResolutionW() + "', '" + temp.getResolutionH() + "', '" + temp.getVolume() + "');");
                System.out.println("Inserted " + temp.getResolutionW() + ", " + temp.getResolutionH() + ", " + temp.getVolume());
            c.commit();
            s.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + " at SaveSettings:" + e.getMessage());
            System.exit(1);
        }
    }
}
