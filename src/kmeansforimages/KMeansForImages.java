/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kmeansforimages;

/**
 *
 * @author Preethi
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;


public class KMeansForImages {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length < 3){
	    System.out.println("Usage:<input-image> <k> <output-image>");
	    return;
	}
	try{
            File temp1;
	    BufferedImage originalImage = ImageIO.read(temp1=new File(args[0]));
	    int k=Integer.parseInt(args[1]);
	    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
            File temp;
	    ImageIO.write(kmeansJpg, "jpg",temp = new File(args[2])); 
            float origsize = (float) temp1.length();
            float kmeanssize = (float) temp.length();
            System.out.println("Compression:");
            System.out.println(origsize/kmeanssize);
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}
    }
        private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
            
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	int[] rgb=new int[w*h];
	int count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
	    }
	}
	// Call kmeans algorithm: update the rgb values
	kmeans(rgb,k);

	// Write the new rgb values to the image
	count=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }

    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k)
    {
        int mean_values[]= new int[k];
        boolean hash_values[]= new boolean[rgb.length];
        Random random=new Random();
        int centroid_values[]=new int[rgb.length];
        
        for(int i=0;i<k;i++)
        {
            int temp = random.nextInt(rgb.length);
            if(hash_values[temp])
            {
                continue;
            }
            mean_values[i]=rgb[temp];
            hash_values[temp]=true;
        }
        for(int i=0;i<100;i++)
        {
            for(int j=0;j<rgb.length;j++)
            {
                int cluster_value = -1;
                int minimum = Integer.MAX_VALUE;
                for(int l=0;l<k;l++)
                {
                    Color pixel = new Color(rgb[j]);
                    Color region = new Color(mean_values[l]);
                    double r = Math.pow(pixel.getRed()-region.getRed(),2);
                    double g = Math.pow(pixel.getGreen()-region.getGreen(),2);
                    double b = Math.pow(pixel.getBlue()-region.getBlue(),2);
                    
                    int d= (int) Math.sqrt(r+g+b);
                    if(d<minimum)
                    {
                        minimum = d;
                        cluster_value = l;
                    }
                }
                centroid_values[j]=cluster_value;
            }
            //updation
            for(int h=0;h<k;h++)
        {
            int p=0,r=0,g=0,b=0;
            for(int j=0;j<rgb.length;j++)
            {
                if(centroid_values[j]==h)
                {
                    Color pixel = new Color(rgb[j]);
                    r=r+pixel.getRed();
                    g=g+pixel.getGreen();
                    b=b+pixel.getBlue();
                    p++;
                }
            }
            if(p>0)
            {
                Color newpi= new Color(r/p,g/p,b/p);
                mean_values[h]=newpi.getRGB();
            }
        }
        }
        for(int i=0;i<rgb.length;i++)
        {
            rgb[i]=mean_values[centroid_values[i]];
        }
    }
    
}
