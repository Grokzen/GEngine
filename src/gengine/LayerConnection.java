package gengine;

public class LayerConnection {
    private Layer first = null;
    private Layer second = null;
    private CollisionReport reportTo = null;
    
    public LayerConnection(Layer f, Layer s, CollisionReport cr) {
        first = f;
        second = s;
        reportTo = cr;
    }
    
    public void processConnection() {
        if (first != null && second != null) {
            for (Entity e : first.getEntList() ) {
                for (Entity en : second.getEntList() ) {
                    if (e != en) {
                        if (e.intersect(en.getRect() ) ) {
                            report(e, en);
                        }
                    }
                }
            }
        }
    }
    
    private void report(Entity baseEnt, Entity targetEnt) {
        reportTo.collisionDetected(baseEnt, targetEnt);
    }
}
