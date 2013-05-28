package gengine;

public class Node {
    private double x = 0;
    private double y = 0;
    
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}
