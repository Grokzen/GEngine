package gengine;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

public class Engine {
    private JFrame container = null;
    private int frameW = -1, frameH = -1;
    
    private Graphics graphics = null;
    private Graphics2D g = null;
    private Canvas canvas =  null;
    private BufferStrategy strategy = null;
    private BufferedImage bi = null;
    
    private ArrayList<LayerConnection> collisionConnections = null;
    private ArrayList<Layer> gameLayers = null;
    private ArrayList<Task> tasks = null;
    private Task tmpTask = null;
    
    public static long lastTic = 0;
    
    /**
     * GFX & basic game engine
     * 
     * @param frameName JFramens namn, även spelets namn
     * @param dim JFramens Dimensioner, Widht + Height
     */
    public Engine(String frameName, int width, int height) {
        frameW = width;
        frameH = height;
        init(frameName);
    }
    
    /**
     * Initzierar alla variablar, skapar även fönstret som spelet körs i
     * 
     * @param frameName JFramens namn, även spelets namn
     * @param dim JFramens Dimensioner, Widht + Height
     */
    public void init(String frameName) {
        container = new JFrame(frameName);
        container.setIgnoreRepaint(true);
        container.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        container.setResizable(false);
        container.setUndecorated(true);
        
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        canvas = new Canvas();
        canvas.setIgnoreRepaint(true);
        canvas.setSize(frameW, frameH);
        
        container.add(canvas);
        container.pack();
        container.setVisible(true);
        
        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        bi = gc.createCompatibleImage(frameW, frameH);
        
        
        SpriteStore.makeSpriteStore();
        collisionConnections = new ArrayList<LayerConnection>();
        gameLayers = new ArrayList<Layer>();
        tasks = new ArrayList<Task>();
        setupEntitys();
    }
    
    /**
     * Render metoden som uppdaterar och ritar ut allting på skärmen
     */
    private Color background = Color.PINK;
    public void render() {
        System.out.println("RenderLoopBegin");
        try {
            g = bi.createGraphics();
            g.setColor(background);
            g.fillRect(0, 0, frameW, frameH);
            /************/
            
            for (int i = 0; i < tasks.size(); ) {
                tmpTask = tasks.get(i);
                tmpTask.executeTask();

                if (tmpTask.getTaskDone() ) {
                    tasks.remove(tmpTask);
                    //System.out.println("TASK DONE");
                }
                else {
                    i++;
                }
            }

            for (Layer L : gameLayers) {
                for (Entity e : L.getEntList() ) {
                    e.draw(g);
                }
            }
            
            for (LayerConnection lc : collisionConnections) {
                lc.processConnection();
            }
            
            /************/
            graphics = strategy.getDrawGraphics();
            graphics.drawImage(bi, 0, 0, null);
            
            if (!strategy.contentsLost() )
                strategy.show();
            
            System.out.println("RenderLoopStop");
        }
        finally {
            if (graphics != null) {
                graphics.dispose();
            }
            if (g != null) {
                g.dispose();
            }
        }
    }
    
    public void setLastTic(long time) {
        lastTic = time;
        System.out.println ("LastTic: " + lastTic);
    }
    
    /**
     * 
     * @param firstLayer
     * @param secondLayer
     * @param reportTo Om man hittar någon collision mellan de entitys som finns i lagren man 
     *  loopar i så rappoteras fyndet till den klassen
     */
    public void makeCollisionConnection(String firstLayer, String secondLayer, CollisionReport reportTo) {
        if (firstLayer != null || secondLayer != null || reportTo != null) {
            LayerConnection lc = new LayerConnection( findLayerByName(firstLayer), findLayerByName(secondLayer), reportTo);
            collisionConnections.add(lc);
            System.out.println("*Addat En Connection Mellan Lagren: " + firstLayer + " : " + secondLayer + " | Rappoteras till: " + reportTo.getClass().getName() );
        }
    }
    
    
    /**
     * Addar en Task till listan över alla tasks som skall utföras i render metoden
     * 
     * @param t är ett Interfacat Task object som kan vara vilken interfacad subklass som hellst
     */
    public void addTask(Task t) {
        tasks.add(t);
    }
    
    /**
     * Ful Metod, skall ej vara final till slutverisionen
     * Test metod enbart under testfasen
     */
    public void setupEntitys() {
        createNewLayer("player");
        createNewLayer("enemy");
        Entity e;
        
        e = new Entity(1, 10, 10, "test.jpg");
        addEntityToLayer(e, "player");
        
        e = new Entity(2, 50, 50, "test2.jpg");
        addEntityToLayer(e, "enemy");
    }
    
    /**
     * 
     * @param taskEntID
     */
    public void removeTask(int taskEntID) {
        Task toRemove = null;
        
        synchronized (tasks) {
            for (Task t : tasks) {
                if (t.getEntityID() == taskEntID) {
                    System.out.println("TASK TO REMOVE FOUND");
                    toRemove = t;
                }
            }
            if (toRemove != null) {
                tasks.remove(toRemove);
            }
        }
    }
    
    /**
     * Flyttar en Entity som en Animation, Från dess nuvarande position till den 
     *  nya som är Nodens position
     * 
     * @param e Entitys som skall flyttas
     * @param n Noden som innehåller målet för entityn
     */
    public void moveEntity(Entity e, Node n) {
        MoveTask mt = new MoveTask(e,n);
        addTask(mt);
    }
    
    /**
     * Flyttar Entityn från sin nuvarande pos till en pos += n.x && += n.y
     * 
     * @param e Entitys som skall flyttas
     * @param n Noden som innehåller målet för entityn 
     */
    public void translateEntity(Entity e, Node n) {
        TranslateTask tt = new TranslateTask(e,n,false);
        addTask(tt);
    }
    
    /**
     * Flyttar Entityn till en Position ej relativt från Entityns nuvarande pos,
     *  utan från game framens kordinat system
     * 
     * @param e Entitys som skall flyttas
     * @param n Noden som innehåller målet för entityn
     */
    public void teleportEntity(Entity e, Node n) {
        TranslateTask tt = new TranslateTask(e,n,true);
        addTask(tt);
    }
    
    /**
     * Letar reda på entityn med det ID som motsvarar inputen
     * 
     * @param ID Det sökta ID:t 
     * @return Retunerar den sökta Entityn, om ingen hittas retuneras en negativt
     *          skapad Entity
     */
    public Entity findEntityGlobal(int ID) {
        for (Layer L : gameLayers) {
            for (Entity e : L.getEntList() ) {
                if (e.getID() == ID) {
                    return e;
                }
            }
        }
        
        return new Entity(-1);
    }
    
    /**
     * Letar efter en Entity i ett speciellt Layer
     * 
     * @param ID ID nummret på den sökta Entityn
     * @param layerName - Namnet på lagret man vill leta i
     * @return Retunerar En Entity
     */
    public Entity findEntity(int ID, String layerName) {
        Layer L = findLayerByName(layerName);
        
        if (L != null) {
            for (Entity e : L.getEntList() ) {
                if (e.getID() == ID) {
                    System.out.println("FoundEntity");
                    return e;
                }
            }
            
            System.out.println("NoEntityFound");
            return new Entity(-1);
        }
        else {
            System.out.println("NoEntityFound");
            return new Entity(-1);
        }
    }
     
    /**
     * Letar efter en Layer som överänstämmer med String inputen
     * 
     * @param searchName Namnet på det sökta Layert
     * @return Retunerar ett lager eller null beroende på resultatet
     */
    public Layer findLayerByName(String searchName) {
        for (Layer L : gameLayers) {
            if (L.getLayerName().equals(searchName) ) {
                return L;
            }
        }
        return null;
    }
    
    /**
     * Kontrollerar och tar bort ett lager om lagret finns i spelet. Alla Entitys
     *  försvinner direkt utan att sparas
     * 
     * @param layerName Namnet på det Layer man vill ta bort
     */
    public void destroyLayer(String layerName) {
        Layer L = findLayerByName(layerName);
        
        if (L != null) {
            gameLayers.remove(L);
            System.out.println("LayerFoundAndRemoved");
        }
        else {
            System.out.println("NoLayerWithThatNameFound");
        }
    }
    
    /**
     * Skapar ett nytt Layer och tilldelar det lagret String inputens Namn,
     *  Finns lagret redan kommer den *Inte* att skapas igen utan metoden gör inget
     * 
     * @param layerName Namnet på lagret man vill skapa
     */
    public void createNewLayer(String layerName) {
        for (Layer L : gameLayers) {
            if (L.getLayerName().equals(layerName) ) {
                System.out.println("LagretExtisterarRedan");
                return;
            }
        }
        
        Layer tmpL = new Layer(layerName);
        gameLayers.add(tmpL);
    }
    
    /**
     * Tar bort en specefik Entity med entID, som sitt ID från lagret som har namnet
     *  som är lika som layerName, existerar inte lagret görs inget
     * 
     * @param entID IDt på den entity man vill ta bort
     * @param layerName Det lagret man vill ta bort den specefika entityn i
     */
    public void destroyEntityFromLayer(int entID, String layerName) {
        Layer L = findLayerByName(layerName);
            
        if (L.getLayerName().equals(layerName) && L != null) {
            System.out.println("FoundLayerToRemoveFrom");
            L.removeEnt(entID);
        }
    }
    
    /**
     * Addar Entityn "e" till det lagret som har namnet "layerName"
     * 
     * @param e Entityn man vill adda till Layert
     * @param layerName Namnet på det lager man vill adda Entityn till
     */
    public void addEntityToLayer(Entity e, String layerName) {
        Layer L = findLayerByName(layerName);
        
        if (L.getLayerName().equals(layerName) && L != null ) {
            System.out.println("FoundLayerMatch AddEntityToLayer");
            L.addEntity(e);
        }
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
}
