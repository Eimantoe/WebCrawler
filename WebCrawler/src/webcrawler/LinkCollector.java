package webcrawler;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class LinkCollector {
    
    private ArrayList<String> totalLinks = new ArrayList<>();
    private ArrayList<String> targetedLinks = new ArrayList<>();
    private static int index;
    private ArrayList<String> unwantedLinks;
    private int links_to_crawl = 0;
    private String outputFile = "Output";
    private String phrase = "";
    private String wiki_lang = "";
        
    public LinkCollector(){
        entries_to_ignore();
        this.index = 0;
    }
    
    public void entries_to_ignore(){
        try {
            String words = new String(Files.readAllBytes(Paths.get("entries to ignore")));
            unwantedLinks = new ArrayList<>(Arrays.asList(words.split("\\s+")));
        } catch (IOException ex) {
            Logger.getLogger(LinkCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void scrapWiki( String link ){
        WebCrawler.gui.setStatusLabel("Running");
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
            org.jsoup.select.Elements links = doc.select("a");
            int i = 0;
            String s[] = new String[links.size()];
            for(Element e: links){
                s[i++]= e.attr("abs:href"); 
            }
            for(String element: s){
                if(!isDuplicate(element) && legitLink(element)){   
                    totalLinks.add(element);
                    //out.println(totalLinks.size()); 
                    org.jsoup.select.Elements body = doc.select("p");
                    for(Element el : body ){
                        if(el.ownText().toLowerCase().contains(phrase)){
                            targetedLinks.add(element);
                            break;
                        }
                    }
                }
            }
        }catch(java.net.MalformedURLException urle ){
            out.println("urle exception caught");
        }catch(java.lang.IllegalArgumentException arge){
            out.println("arge exception caught");
        }catch(org.jsoup.HttpStatusException httpe){
            out.println("httpe exception caught");
        }catch(org.jsoup.UnsupportedMimeTypeException mimetypee){
            out.println("mimetype exception caught");
        }catch(java.net.SocketTimeoutException timeoutex){
            out.println("timeout exception caught");
        }catch(java.net.ConnectException ex ){
            out.println("connect exception");
        }catch (IOException ex) {
            out.println("IO exception caught");
        }
        if( index < totalLinks.size() && totalLinks.size() < links_to_crawl  ){
            scrapWiki(totalLinks.get(index++));
        }
    }
    
    public void searchByTitle(String link) {
        WebCrawler.gui.setStatusLabel("Running");
        try{
            org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
            org.jsoup.select.Elements links = doc.select("a");
            int i = 0;
            String s[] = new String[links.size()];

            for(Element e: links){
                s[i++]= e.attr("abs:href");
            }
            for(String element: s){
                if(!isDuplicate(element) && legitLink(element)){   
                    totalLinks.add(element); 
                    out.println(totalLinks.size()); 
                    if( element.toLowerCase().contains(phrase) ){
                        targetedLinks.add(element);
                    }
                }
            }
        }catch(java.net.MalformedURLException urle ){
            out.println("urle exception caught");
        }catch(java.lang.IllegalArgumentException arge){
            out.println("arge exception caught");
        }catch(org.jsoup.HttpStatusException httpe){
            out.println("httpe exception caught");
        }catch(org.jsoup.UnsupportedMimeTypeException mimetypee){
            out.println("mimetypee exception caught");
        }catch(java.net.SocketTimeoutException timeoutex){
            out.println("timeout exception caught");
        }catch(java.net.ConnectException ex ){
            out.println("connect exception");
        }catch (IOException ex) {
            out.println("IO exception caught");
        }
        if( index < totalLinks.size() && totalLinks.size() < links_to_crawl  ){
            searchByTitle(totalLinks.get(index++));
        }
    }
    
    public void searchByBody(String link){
        WebCrawler.gui.setStatusLabel("Running");
        try{
            org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
            org.jsoup.select.Elements links = doc.select("a");
            int i = 0;
            String s[] = new String[links.size()];

            for(Element e: links){
                s[i++]= e.attr("abs:href");
            }
            for(String element: s){
                if(!isDuplicate(element) && legitLink(element)){   
                    totalLinks.add(element);
                    org.jsoup.select.Elements body = doc.select("p");
                    for(Element el : body){
                        if(el.ownText().toLowerCase().contains(phrase.toLowerCase())){
                            targetedLinks.add(element);
                            break;
                        }
                    }
                }
            }
        }catch(java.net.MalformedURLException urle ){
            out.println("urle exception caught");
        }catch(java.lang.IllegalArgumentException arge){
            out.println("arge exception caught");
        }catch(org.jsoup.HttpStatusException httpe){
            out.println("httpe exception caught");
        }catch(org.jsoup.UnsupportedMimeTypeException mimetypee){
            out.println("mimetypee exception caught");
        }catch(java.net.SocketTimeoutException timeoutex){
            out.println("timeout exception caught");
        } catch (IOException ex) {
            out.println("IO exception caught");
        }
        if( index < totalLinks.size() && totalLinks.size() < links_to_crawl  ){  
            searchByBody(totalLinks.get(index++));
        }
    }
    
    public ArrayList getLinkArr(){
        return this.totalLinks;
    }
    
    public ArrayList getTargetedLinksArr(){
        return this.targetedLinks;
    }
    
    public boolean legitLink( String link ){
        for( String el : unwantedLinks ){
            if( link.contains(el) || (link.contains(".wiki") && link.length() == 30 && !link.contains(getWikiLang()) ) ){
                return false;
            }
        }
        return true;
    }
    
    public boolean isDuplicate( String link ){
        if( totalLinks.contains(link)){
            return true;
        } else {
            return false;
        }
    }
    
    public void setPhrase( String word ){
        this.phrase = word;
    }
    
    public String getPhrase(){
        return this.phrase;
    }
    
    public void setOutputFile( String filename ){
        this.outputFile = filename;
    }
    
    public String getOutputFile( String filename ){
        return this.outputFile;
    }
    
    public void setLinksToCrawl( int number ){
        this.links_to_crawl = number;
    }
    
    public void setWikiLang( String url ){
        this.wiki_lang = url.substring(url.indexOf("://")+2, url.indexOf("wiki"));
    }
    
    public String getWikiLang(){
        return this.wiki_lang;
    }
    
    public void clearWikiLang(){
        this.wiki_lang = "";
    }
    
    public void writeToFile(){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputFile), "utf-8"))) {
            writer.write("Crawled url : " + WebCrawler.gui.getURLField() + "\n" +
                         "Was searching for : " + WebCrawler.gui.getKeywordField() + "\n" +
                         "Targeted entries found : " + targetedLinks.size() + "\n");
            for( String entry : targetedLinks ){
                writer.write(entry+"\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(LinkCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void openFile(){
        try {
            Desktop.getDesktop().open(new File(this.outputFile));
        } catch (IOException ex) {
            Logger.getLogger(LinkCollector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resetFields(){
        WebCrawler.gui.cleanURLField();
        WebCrawler.gui.cleanKeywordField();
        WebCrawler.gui.cleanOutputField();
        WebCrawler.gui.cleanNoLinks();
        clearWikiLang();
        totalLinks.clear();
        targetedLinks.clear();
        WebCrawler.gui.setStatusLabel("Ready");
        this.index = 0;
    }
    
}
