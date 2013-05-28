package gengine;

import java.util.ArrayList;

public class Layer {
    private ArrayList<Entity> entitys;
    private String layerName;
    
    public Layer(String name) {
        init(name);
    }
    
    public void init(String layerN) {
        entitys = new ArrayList<Entity>();
        layerName = layerN;
    }
    
    public String getLayerName() {
        return layerName;
    }
    
    public ArrayList<Entity> getEntList() {
        return entitys;
    }
    
    public void addEntity(Entity ent) {
        entitys.add(ent);
    }
    public void removeEnt(int ID) {
        for (Entity ent : entitys) {
            if (ent.getID() == ID) {
                System.out.println("FoundEntityToRemove");
            }
        }
    }
}