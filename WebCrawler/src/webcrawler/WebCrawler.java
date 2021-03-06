package webcrawler;

import java.io.IOException;
import static java.lang.System.out;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

/**
 *
 * @author eimantas
 */
final public class WebCrawler {
      
    public static GUI               gui;
    public static LinkCollector     linkCollector;
    
    public static void main(String[] args){

        try 
        { 
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"); 
        } 
        catch(Exception e)
        { 
        }

        gui             = new GUI();
        linkCollector   = new LinkCollector();
    }
    
}