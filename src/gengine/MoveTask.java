package gengine;

public class MoveTask implements Task {
    private Node n = null;
    private Entity movingEnt = null;
    private boolean taskDone = false;
    private double xMoveTic = 0.0;
    private double yMoveTic = 0.0;
    private double distance = 0.0;
    
    public MoveTask(Entity e, Node n) {
        this.n = n;
        movingEnt = e;
    }
    
    public void executeTask() {
        
        
        
        /*distance = (movingEnt.getMoveSpeed() * Engine.lastTic) / 1000;
        xMoveTic = Math.abs(distance) - movingEnt.getX();
        yMoveTic = Math.abs(distance) - movingEnt.getY();
        
        System.out.println("Ent: " + movingEnt);
        movingEnt.translateXY(xMoveTic, yMoveTic);
        //System.out.println("TickCalc: " + (Engine.lastTic / 1000) );
        System.out.println("Distance: " + distance + " XMove: " + xMoveTic + " YMove: " + yMoveTic + "\n");
        //System.out.println("EntPos: " + movingEnt.getX() + ":" + movingEnt.getY() );
        movingEnt.translateX(1);
        
        if (movingEnt.getX() == getDestX() ) {
            taskDone = true;
        }*/
    }
    
    public int getEntityID() {
        return movingEnt.getID();
    }
    
    public boolean getTaskDone() {
        return taskDone;
    }
    
    public Entity getMovingEnt() {
        return movingEnt;
    }
    
    public double getDestX() {
        return n.getX();
    }
    public double getDestY() {
        return n.getY();
    }
}
