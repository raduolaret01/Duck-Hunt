
import java.sql.*;



public class DataManager {

    private static int getRowCount(ResultSet resultSet) {
        if (resultSet == null) {
            return 0;
        }

        try {
            resultSet.last();
            return resultSet.getRow();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            try {
                resultSet.beforeFirst();
            } catch (SQLException exp) {
                exp.printStackTrace();
            }
        }

        return 0;
    }

    public static Texture[] LoadTextureInfo(){
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
}
