package gengine;

public class TranslateTask implements Task {
    private Node n;
    private Entity ent;
    private boolean taskDone = false;
    private boolean teleport;
    
    public TranslateTask(Entity e, Node n, boolean teleport) {
        this.ent = e;
        this.n = n;
        this.teleport = teleport;
    }
    
    public void executeTask() {
        if (teleport) {
            ent.teleport(n.getX(), n.getY() );
        }
        else {
            ent.translateXY(n.getX(), n.getY() );
        }
        
        taskDone = true;
    }
    
    public int getEntityID() {
        return ent.getID();
    }
    
    public boolean getTaskDone() {
        return taskDone;
    }
}
