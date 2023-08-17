package it.unipd.dei.eis.fra;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *  Classe che gestisce il download degli articoli da The Guardian tramite le GuardianAPI.
 *  DownloadGuardian fa uso dei metodi storeArticles e getFolder forniti da Download
 *  @see it.unipd.dei.eis.fra.Download
 */
public class DownloadGuardian extends Download{

    // Chiave API per accedere ai dati di The Guardian
    private final String apikey = "5d63d9e8-4ca4-49e4-9d00-0fba1a197317";

    // Nome della cartella in cui verranno memorizzati gli articoli
    private String folderName = "ArticoliGuardian";

    // Stringa di query per l'API di The Guardian
    private String queryString;

    /**
     * Costruttore predefinito con query di default ("nuclear power").
     */
    public DownloadGuardian(){
        String query = "nuclear+power";
        this.queryString = "https://content.guardianapis.com/search?q="+query+"&show-fields=bodyText&page-size=200&page=";
    }

    /**
     * Costruttore con query personalizzata.
     *
     * @param query La query di ricerca per gli articoli inserita dall'utente.
     */
    public DownloadGuardian(String query){
        String encodedQuery = query;
        try{
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch(UnsupportedEncodingException e){
            System.out.println("Errore, encoding non supportato");
        }
        this.queryString = "https://content.guardianapis.com/search?q="+encodedQuery+"&show-fields=bodyText&page-size=200&page=";
    }

    /**
     * Scarica gli articoli da The Guardian e li memorizza nella cartella corrispondente.
     */
    public void downloadAndStore(){
        List<String> articlesBody = downloadArticles();
        storeArticles(articlesBody, this.folderName);
    }

    /**
     * Scarica gli articoli del The Guardian dal web tramite le GuardianAPI usando HttpURLConnection
     * @see HttpURLConnection
     * @return Lista di stringhe contenenti il corpo degli articoli
     */
    protected List<String> downloadArticles() {
        int page = 1;
        int numOfPages = 1;
        List<String> articlesBody = new ArrayList<>();
        do {
            // Crea una connessione HTTP per l'URL dell'API del Guardian
            int x = queryString.indexOf("page=");
            queryString = queryString.substring(0, x + 5);
            queryString = queryString + "" + page + "&api-key=" + this.apikey;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(queryString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
            } catch (IOException i) {
                System.out.println("Errore nell'apertura della connessione");
                i.printStackTrace();
            } catch (Exception e) {
                System.out.println("Errore nella creazione della connessione");
                e.printStackTrace();
            }

            // Ottieni la risposta JSON dall'API del Guardian
            InputStream inputStream = null;
            JSONObject jsonResponse = null;
            try {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                    responseBuilder.append(System.lineSeparator());
                    jsonResponse = new JSONObject(responseBuilder.toString());
                }
                // Chiusura del BufferedReader
                reader.close();
            } catch (IOException e) {
                System.out.println("Errore nell'apertura o lettura dall'InputStream della connessione. Controlla la tua connessione ad internet e di aver inserito la tua Guardian API Key.");
                return articlesBody;
            }

            // Chiusura della connessione
            connection.disconnect();

            // Parsing della risposta JSON e salvataggio del corpo degli articoli in una List<String>
            JSONArray articles = jsonResponse.getJSONObject("response").getJSONArray("results");
            numOfPages = jsonResponse.getJSONObject("response").getInt("pages");
            int numOfArticles = jsonResponse.getJSONObject("response").getInt("total");
            if (numOfArticles == 0) {
                System.out.println("Non è stato trovato nessun articolo del Guardian con questa query di ricerca");
                return articlesBody;
            } else {
                for (int i = 0; i < 200 && i < (numOfArticles - 200 * (1 - page)); i++) {
                    try {
                        JSONObject article = articles.getJSONObject(i);
                        articlesBody.add(article.getJSONObject("fields").getString("bodyText"));
                    } catch (Exception e) {
                        System.out.println("Errore nell'estrazione dei dati dal JSON");
                        e.printStackTrace();
                    }
                }
            }
            page++;
        } while (page < 6 && page <= numOfPages);
        return articlesBody;
    }
}
