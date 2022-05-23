package global;

import java.sql.*;



public class DataManager {

    private static int numberOfScoreEntries = 0;

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
            ResultSet select = s.executeQuery("SELECT * FROM TopScores;");
            numberOfScoreEntries = 0;
            while(select.next()){
                ++numberOfScoreEntries;
            }
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(1);
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
            return tempAtlas;
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static boolean checkScore(int score){
        if(numberOfScoreEntries < 10) {
            return true;
        }
        else{
            Connection c = null;
            Statement s = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
                c.setAutoCommit(false);
                s = c.createStatement();
                ResultSet select = s.executeQuery("SELECT Score FROM TopScores ORDER BY Score;");
                select.next();
                if(score > select.getInt(3)){
                    return true;
                }
                return false;
            }
            catch (Exception e) {
                System.err.println(e.getClass().getName() + ":" + e.getMessage());
                System.exit(1);
            }
        }
        return false;
    }

    public static void InsertNewScore(ScoreEntry entry){
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();
            s.execute("INSERT INTO TopScores (Name, Score) VALUES ('" + entry.name + "', '" + entry.score + "');");
            ++numberOfScoreEntries;

            ResultSet select = s.executeQuery("SELECT * FROM TopScores ORDER BY Score DESC;");
            select.next();

            for(int i = 1; i < numberOfScoreEntries; ++i){
                s.execute("UPDATE TopScores SET Score = '" + select.getInt(3) + "', Name = '" + select.getString(2) + "' WHERE Id = '" + i + "';");
                select.next();
            }
            if(numberOfScoreEntries > 10){
                s.execute("DELETE FROM TopScores WHERE Id = '11';");
            }

        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(1);
        }
    }

    public static ScoreEntry[] getScores(){
        Connection c = null;
        Statement s = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:rsc/database/database.db");
            c.setAutoCommit(false);
            s = c.createStatement();

            ScoreEntry[] temp = new ScoreEntry[numberOfScoreEntries];
            ResultSet select = s.executeQuery("SELECT * FROM TopScores ORDER BY Score DESC;");
            while(select.next()){
                temp[select.getInt(1) - 1] = new ScoreEntry(select.getString(2), select.getInt(3));
            }
            return temp;
        }
        catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
