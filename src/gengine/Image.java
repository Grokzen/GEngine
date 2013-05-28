package gengine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;
import javax.imageio.*;

/**
 * An Image wrapper
 * @author Niklas Myrberg
 */
public class Image {
	private java.awt.image.BufferedImage m_img = null;
	private int m_width;
	private int m_height;
	
        /**
	 * Load an image as a Compatible Image (this is done automatically when using this Image class) 
	 * @param location location of the resource
	 * @return A Compatible Buffered Image
	 * @throws IOException
	 */
	public static BufferedImage loadCompatibleImage(String location) throws IOException 
	{
            InputStream is = location.getClass().getResourceAsStream(location);
            return loadCompatibleImage(is);
	}	
        
	/**
	 * Load an image as a Compatible Image (this is done automatically when using this Image class) 
	 * @param resource InputStream connected to the image resource
	 * @return A Compatible Buffered Image
	 * @throws IOException
	 */
	public static BufferedImage loadCompatibleImage(InputStream resource) throws IOException 
	{
		BufferedImage image = ImageIO.read(resource);
		
		GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		BufferedImage compatibleImage = configuration.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		Graphics g = compatibleImage.getGraphics();
		
		g.drawImage(image, 0, 0, null);
		g.dispose();		
		
		return compatibleImage;
	}	
	
	/**
	 * Load an image as a Compatible Image (this is done automatically when using this Image class) 
	 * @param url URL to the image
	 * @return A Compatible Buffered Image
	 * @throws IOException
	 */
	public static BufferedImage loadCompatibleImage(URL url) throws IOException 
	{
		BufferedImage image = ImageIO.read(url);
		
		GraphicsConfiguration configuration = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		BufferedImage compatibleImage = configuration.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		Graphics g = compatibleImage.getGraphics();
		
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return compatibleImage;
	}
	
	/**
	 * Convert an image to a Compatible Image (this is done automatically when using this Image class) 
	 * @param image Image to convert
	 * @return A Compatible Buffered Image
	 * @throws IOException
	 */
	public static BufferedImage convertToCompatibleImage(BufferedImage image) throws IOException 
	{
		GraphicsConfiguration configuration = GraphicsEnvironment.
		getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		
		BufferedImage compatibleImage = configuration.createCompatibleImage(image.getWidth(), image.getHeight(), Transparency.TRANSLUCENT);
		Graphics g = compatibleImage.getGraphics();
		
		g.drawImage(image, 0, 0, null);
		g.dispose();
		
		return compatibleImage;
	}	
	
	/**
	 * Creates an Image as a subimage of another Image
	 * @param source the source Image
	 * @param x x position of the subimage
	 * @param y y position of the subimage
	 * @param width width of the subimage
	 * @param height height of the subimage
	 */
	public Image(Image source, int x, int y, int width, int height)
    {
		BufferedImage img = source.getBufferedImg().getSubimage(x, y, width, height);
		
        try {
			m_img = convertToCompatibleImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
        m_width = width;
        m_height = height;
    }
	
	/**
	 * Creates an Image as a subimage of a java.awt.image.BufferedImage
	 * @param source the source java.awt.image.BufferedImage
	 * @param x x position of the subimage
	 * @param y y position of the subimage
	 * @param width width of the subimage
	 * @param height height of the subimage
	 */	
	public Image(java.awt.image.BufferedImage source, int x, int y, int width, int height)
    {
		BufferedImage img = source.getSubimage(x, y, width, height);

		try {
			m_img = convertToCompatibleImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		} 

        m_width = width;
        m_height = height;
    }

	/**
	 * Creates an Image from a resource
	 * @param location location of the resource
	 * @throws IOException
	 */
	public Image(String location) throws IOException
	{
        if(location == null)
        	throw new IOException();
        
        try
        {
            InputStream is = getClass().getResourceAsStream(location);
            
            m_img = loadCompatibleImage(is);
        }
        catch(IOException e) {
            throw e;
        }
        
            m_height = m_img.getHeight();
            m_width = m_img.getWidth();
	}

	/**
	 * Creates an Image as a subimage of a resource
	 * @param source the source Image
	 * @param x x position of the subimage
	 * @param y y position of the subimage
	 * @param width width of the subimage
	 * @param height height of the subimage
	 * @throws IOException
	 */
	public Image(String location, int x, int y, int width, int height) throws IOException
    {
        if(location == null)
        	throw new IOException();
        
        BufferedImage source = null;
        
        try
        {
        	InputStream is = getClass().getResourceAsStream(location);
        	source = loadCompatibleImage(is);
        }
        catch(IOException e) {
            throw e;
        }

		BufferedImage img = source.getSubimage(x, y, width, height);
		
        try {
			m_img = convertToCompatibleImage(img);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
        m_width = width;
        m_height = height;
    }	
	
	
	/**
	 * Creates an Image from a URL
	 * @param url URL of image
	 * @throws IOException
	 */
	public Image(URL url) throws IOException
	{
        if(url == null)
        	throw new IOException();
        
        try
        {
            m_img = loadCompatibleImage(url);
        }
        catch(IOException e) {
            throw e;
        }
        
        m_height = m_img.getHeight();
        m_width = m_img.getWidth();		
	}
	
	
	/**
	 * Runs the image through a BufferedImageOp filter
	 * @param filter The filter to use
	 */
	public void filter(BufferedImageOp filter)
	{
		BufferedImage image = filter.createCompatibleDestImage(m_img, null);
		filter.filter(m_img, image);
		m_img = image;
	}
	
	/**
	 * Draws the Image
	 * @param g The Graphics2D to draw on
	 * @param x x position of where to draw the image
	 * @param y y position of where to draw the image
	 */
	public void draw(Graphics2D g, int x, int y)
	{
		if(m_img != null)
			g.drawImage(m_img, x, y, null);
	}
        
        /**
         * Draws the Image
         * @param g The Graphics to draw on
         * @param x x position of where to draw the image
         * @param y y position of where to draw the image
         * @param w The width of the image to draw
         * @param h The height of the image to draw
         */
        public void draw(Graphics2D g, int x, int y, int w, int h) {
            if (m_img != null) {
                g.drawImage(m_img, x, y, w, h, null);
            }
        }

	/**
	 * Draws the Image
	 * @param g The Graphics2D to draw on
	 * @param at Transform describing how and where the image should be drawn
	 */
	public void draw(Graphics2D g, AffineTransform at)
	{
		if(m_img != null)
			g.drawImage(m_img, at, null);
	}	
	
	/**
	 * Get the raw image data 
	 * @return A WriteableRaster with the data
	 */
	public WritableRaster getImageData() {
		return m_img.getRaster();
	}
	
	/**
	 * Get the width of the image
	 * @return width of the image
	 */
	public int getWidth() {
		return m_width;
	}

	/**
	 * Get the height of the image
	 * @return height of the image
	 */	
	public int getHeight() {
		return m_height;
	}
	
	/**
	 * Get the raw image
	 * @return a image of type java.awt.Image 
	 */
	public java.awt.Image getImg() {
		return m_img;
	}
	
	/**
	 * Get the raw image
	 * @return a image of type java.awt.image.BufferedImage 
	 */	
	public java.awt.image.BufferedImage getBufferedImg() {
		return m_img;
	}	
}
