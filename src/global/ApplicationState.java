package global;

public abstract class ApplicationState {

    protected long window;
    public void setWindow(long win){
        this.window = win;
    }
    protected Renderer renderContext = Renderer.getInstance();
    protected Cursor pointer = Cursor.getInstance();
    protected Tile background;

    protected abstract void init();
    protected abstract int loop();
}
