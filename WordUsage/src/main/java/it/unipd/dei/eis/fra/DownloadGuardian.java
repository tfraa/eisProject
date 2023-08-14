package it.unipd.dei.eis.fra;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class DownloadGuardian extends Download{
    String apikey = "5d63d9e8-4ca4-49e4-9d00-0fba1a197317";
    String folderName = "ArticoliGuardian";
    int numberOfArticles = 200;
    String queryString;

    public DownloadGuardian(){
        String query = "nuclear%20power";
        this.queryString = "https://content.guardianapis.com/search?q="+query+"&show-fields=bodyText&page-size=200&page=";
    }

    public DownloadGuardian(String query){
        String encodedQuery = query;
        try{
            encodedQuery = URLEncoder.encode(query, "UTF-8");
        } catch(UnsupportedEncodingException e){
            System.out.println("Errore, encoding non supportato");
            e.printStackTrace();
        }
        this.queryString = "https://content.guardianapis.com/search?q="+encodedQuery+"&show-fields=bodyText&page-size=200&page=";
    }

    public void downloadAndStore(){
        List<String> articlesBody = downloadArticles();
        storeArticles(articlesBody, this.folderName);
    }

    /**
     * @return Returns a List of Strings of all the articles' bodies
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
                System.out.println("I/O error while opening the connection");
                i.printStackTrace();
            } catch (Exception e) {
                System.out.println("Some type of error occured while creating the connection");
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
                System.out.println("I/O Error occured while creating or reading from the input stream");
                e.printStackTrace();
            }

            // Chiusura della connessione
            connection.disconnect();

            // Parsing della risposta JSON e salvataggio del corpo degli articoli in una List<String>

            JSONArray articles = jsonResponse.getJSONObject("response").getJSONArray("results");
            numOfPages = jsonResponse.getJSONObject("response").getInt("pages");
            int numOfArticles = jsonResponse.getJSONObject("response").getInt("total");
            if (numOfArticles == 0) {
                System.out.println("Non è stato trovato nessun articolo del Guardian con questa query di ricerca");
                break;
            } else {
                for (int i = 0; i < 200 && i < (numOfArticles - 200 * (1 - page)); i++) {
                    try {
                        JSONObject article = articles.getJSONObject(i);
                        articlesBody.add(article.getJSONObject("fields").getString("bodyText"));
                    } catch (Exception e) {
                        System.out.println("Error while getting data from JSON");
                        e.printStackTrace();
                    }
                }
            }
            page++;
        } while (page < 6 && page <= numOfPages);
        return articlesBody;
    }
}
