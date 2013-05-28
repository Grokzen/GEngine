package gengine;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Entity {
    private int ID = 0;
    private Image img = null;
    private Node loc = null;
    private Rectangle rect = null;
    private boolean active = false;
    private double moveSpeed = 50.0;
    
    public Entity(int ID) {
        this.ID = ID;
    }
    
    public Entity(int ID, int x, int y, String ref) {
        this.ID = ID;
        this.img = SpriteStore.get().getImage(ref);
        this.rect = new Rectangle(x, y, img.getWidth(), img.getHeight() );
        loc = new Node(x,y);
    }
    
    public double getMoveSpeed() {
        return moveSpeed;
    }
    
    public boolean intersect(Rectangle him) {
        return getRect().intersects(him);
    }
    
    public Rectangle getRect() {
        rect.setLocation( (int)loc.getX(), (int)loc.getY() );
        return rect;
    }
    
    public void draw(Graphics2D g) {
        img.draw(g, (int)loc.getX(), (int)loc.getY() );
    }
    
    public void teleport(double x, double y) {
        loc.setX(x);
        loc.setY(y);
    }
    public void translateXY(double x, double y) {
        translateX(x);
        translateY(y);
    }
    public void translateX(double x) {
        loc.setX( (loc.getX() + x) );
    }
    public void translateY(double y) {
        loc.setY( (loc.getY() + y) );
    }
    
    public void setActive(boolean a) {
        active = a;
    }
    public boolean getActive() {
        return active;
    }
    
    public double getX() {
        return loc.getX();
    }
    public double getY() {
        return loc.getY();
    }
    public int getID() {
        return ID;
    }
}
