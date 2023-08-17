package it.unipd.dei.eis.fra;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe per il download e l'archiviazione di articoli da The New York Times (NYT).
 *
 * La classe fa uso dei metodi forniti da Download
 * @see Download
 */
public class DownloadNYT extends Download{

    // Nome della cartella in cui verranno memorizzati gli articoli
    private String folderName = "ArticoliNYT";

    // Nome del file CSV contenente gli articoli
    private String fileName = "nytimes_articles_v2.csv";

    /**
     * Scarica gli articoli da The New York Times (NYT) e li memorizza nella cartella specificata.
     */
    public void downloadAndStore(){
        List<String> articlesBody = downloadArticles();
        storeArticles(articlesBody, this.folderName);
    }

    /**
     * Scarica gli articoli da un file CSV NYT.
     *
     * @return Restituisce una lista di stringhe contenente il corpo degli articoli scaricati.
     */
    protected List<String> downloadArticles() {
        List<String> articles = new ArrayList<>();
        BufferedReader br = null;
        try {
            // Apre il file CSV per la lettura
            br = new BufferedReader(new FileReader(this.fileName));
            br.readLine(); // Salta la prima riga (intestazioni)
        } catch (FileNotFoundException e) {
            System.out.println("Errore. Non è stato possibile trovare o aprire il file della sorgente.");
        } catch (IOException f) {
            System.out.println("Errore del buffered reader nella lettura del file.");
        }

        String line;
        try {
            while (br != null && (line = br.readLine()) != null) {
                String[] values = line.split(",");
                String body = "";
                if (line.indexOf("\"") != -1) {

                    // Estrae il corpo dell'articolo racchiuso tra virgolette
                    int start_body = line.indexOf("\"") + 1;
                    int end_body = line.indexOf("\"", start_body + 1);
                    body = line.substring(start_body, end_body);
                }
                articles.add(body); // Aggiunge il corpo dell'articolo alla lista
            }
        } catch (IOException e) {
            System.out.println("Errore nella lettura della riga tramite readline");
            e.printStackTrace();
        }
        return articles;
    }
}