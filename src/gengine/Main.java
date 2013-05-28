package gengine;

import java.awt.event.*;

public class Main implements CollisionReport {
    private Engine e;
    public static int frames = 0;
    
    public void collisionDetected(Entity firstEnt, Entity secondEnt) {
        firstEnt.teleport(0, 0);
        secondEnt.getID();
    }
    
    public Main() {
        e = new Engine("Test", 640, 480);
        
        e.teleportEntity(e.findEntityGlobal(1),  new Node(350, 60) );
        e.moveEntity( e.findEntityGlobal(2), new Node(350, 350) );
        //e.moveEntity( e.findEntityGlobal(1), new Node(250, 250) );
        //e.translateEntity( e.findEntityGlobal(1), new Node(100,100) );
        
        e.getCanvas().addKeyListener(new KeyInputHandler() );
        e.getCanvas().requestFocus();
        
        e.makeCollisionConnection("player", "enemy", this);
        
        int fps = 0;
        long totalTime = 0;
        long curTime = System.currentTimeMillis();
        long lastTime = curTime;
        
        long lastTime2 = System.currentTimeMillis();
        //long totalLastTime2 = 0;
        
        int loops = 0;
        while (true) {
            //totalLastTime2 += (System.currentTimeMillis() - lastTime2);
            //System.out.println( (System.currentTimeMillis() - lastTime2) );
            e.setLastTic( (System.currentTimeMillis() - lastTime2) );
            lastTime2 = System.currentTimeMillis();
            //System.out.println("Diff: " + (System.currentTimeMillis() - lastTime) );
            
            lastTime = curTime;
            curTime = System.currentTimeMillis();
            totalTime += curTime - lastTime;

            if( totalTime > 1000 ) {
                //System.out.println("TotalTime: " + totalLastTime2 );
                //totalLastTime2 = 0;
                totalTime -= 1000;
                fps = frames;
                frames = 0;
            }
            ++frames;

            e.render();
            
            loops++;
            
            /*if (loops == 50) {
                e.removeTask(2);
            }*/
            
            if (loops == 10000) {
                System.exit(0);
            }
            
            Thread.yield();
            
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException ie) { }
        }
    }
    
    private class KeyInputHandler extends KeyAdapter {        
        private int keyCode = -1;

        public void keyPressed(KeyEvent evt) {
            keyCode = evt.getKeyCode();

            if (keyCode == KeyEvent.VK_A) {
                e.teleportEntity( e.findEntityGlobal(1), new Node(100,100) );
            }
            else if (keyCode == KeyEvent.VK_S) {
                e.teleportEntity( e.findEntityGlobal(1), new Node(450,250) );
            }
            else if (keyCode == KeyEvent.VK_D) {
                e.teleportEntity( e.findEntityGlobal(1), new Node(300,400) );
            }
        }
    }
    
    public static void main(String[] args) {
        //new Main();
        new MoveTest();
    }
}
