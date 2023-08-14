package it.unipd.dei.eis.fra;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


public class DownloadNYT extends Download{
    String folderName = "ArticoliNYT";
    String fileName = "nytimes_articles_v2.csv";

    public void downloadAndStore(){
        List<String> articlesBody = downloadArticles();
        storeArticles(articlesBody, this.folderName);
    }

    protected List<String> downloadArticles() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.fileName));
            br.readLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error. Couldn't find or open the source file.");
            e.printStackTrace();
        } catch (IOException f) {
            System.out.println("Errore del buffered reader nella lettura del file.");
            f.printStackTrace();
        }

        String line;
        List<String> articles = new ArrayList<>();
        try {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String body = "";
                if (line.indexOf("\"") != -1) {
                    int start_body = line.indexOf("\"") + 1;
                    int end_body = line.indexOf("\"", start_body + 1);
                    body = line.substring(start_body, end_body - 1);
                }
                articles.add(body);
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura della riga tramite readline");
            e.printStackTrace();
        }
        return articles;
    }
}