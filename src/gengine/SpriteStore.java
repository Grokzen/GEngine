package gengine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SpriteStore {
    protected HashMap<String, Image> sprites = new HashMap<String, Image>();
    protected int lastIndex = 0;
    protected static SpriteStore single;

    public static SpriteStore get() {
        return single;
    }
    public static void makeSpriteStore() {
        single = new SpriteStore();
    }

    public HashMap getSprites() {
        return sprites;
    }
    
    public SpriteStore() {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream is = cl.getResourceAsStream("gengine/ImageFiles.txt"); 
        BufferedReader IS = new BufferedReader(new InputStreamReader(is) );                  
        
        String input = "";
        
        try {
            while ( (input = IS.readLine() ) != null) {
                sprites.put(input, new Image("Sprites/" + input) );
            }
        }
        catch (IOException ioe) { ioe.printStackTrace(); }        
        
        System.out.println("SpriteStoreDone");
    }
    
    public Image getImage(String ref) {
        lastIndex = ref.lastIndexOf("/");
        if (lastIndex != -1) {
            return sprites.get(ref.substring(lastIndex + 1) );
        }
        else {
            return sprites.get(ref);
        }        
    }

    private void fail(String message) {
        System.err.println(message);
        System.exit(0);
    }
}