package global;

public class ScoreEntry implements Comparable<ScoreEntry> {
    public String name;
    public int score;

    public ScoreEntry(String name, int score){
        this.name = name;
        this.score = score;
    }

    @Override
    public int compareTo(ScoreEntry o) {
        return this.score - o.score;
    }
}
