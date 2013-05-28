package gengine;

public class MoveTest {
    public double baseX = 10;
    public double baseY = 27;
    public double targetX = 100;
    public double targetY = 100;
    
    public double entX = 50;
    public double entY = 50;
    
    public double ticDist = 0.0;
    public double entMoveDist = 50.0; // hämtas från en Entity
    public double lastTic = 20; //Defineras i game loopen
    
    public double t = 0.0;
    public double helaX = 0.0;
    public double helaY = 0.0;
    public double helaZ = 0.0;
    
    public double y = 0.0;
    public double x = 0.0;
    
    public MoveTest() {
        helaX = Math.abs( (baseX - targetX) );
        helaY = Math.abs( (baseY - targetY) );
        helaZ = Math.sqrt( (helaX * helaX) + (helaY * helaY) );
        t = Math.toDegrees( Math.asin( (1 / helaZ) * helaX) );
        
        process();
    }
    
    public void process() {
        ticDist = (entMoveDist * (lastTic / 1000) );
        
        y = ( (ticDist / 1) * Math.sin( Math.toRadians(t) ) );
        x = ( (ticDist / 1) * Math.sin( Math.toRadians( (90 - t) ) ) );
        
        System.out.println("HelaX: " + helaX + " HelaY: " + helaY + " HelaZ: " + helaZ);
        System.out.println("TicDist: " + ticDist);
        System.out.println("T: " + t + " | " + y + " | " + x + " | " + (Math.sqrt( (x*x) + (y*y) ) ) );
    }
}
