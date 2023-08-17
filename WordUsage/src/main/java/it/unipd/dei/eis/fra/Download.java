package it.unipd.dei.eis.fra;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Classe astratta che fornisce alle classi figlie metodi per l'archiviazione degli articoli e il
 * metodo astratto downloadArticles()
 */
public abstract class Download{
    String folderName= "Articoli";

    /**
     * Metodo astratto per il download degli articoli su cui le classi figlie faranno Overriding
     * @return Una lista di stringhe rappresentanti il corpo degli articoli scaricati
     */
    protected abstract List<String> downloadArticles();

    /**
     * Archivia il corpo degli articoli scaricati in file di testo
     *
     * @param articlesBodyList Una lista di stringhe rappresentanti il corpo degli articoli scaricati
     * @param folderName Nome della cartella in cui salvare gli articoli
     */
    protected void storeArticles(List<String> articlesBodyList, String folderName){
        int i = 1;
        if(articlesBodyList.isEmpty()){
            System.out.println("0 articoli salvati.");
            return;
        }

        // Ottiene il percorso della cartella in cui verranno archiviati gli articoli
        Path p = getFolder(folderName);

        // Itera attraverso la lista dei contenuti degli articoli da archiviare
        for(String body : articlesBodyList){
            File file = new File(p + "/Articolo" + i + ".txt");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }

                // Scrive il corpo dell'articolo nel file
                FileWriter writer = new FileWriter(file);
                writer.write(body);
                writer.close();
            } catch (IOException e) {
                System.out.println("Errore nel salvataggio su file del corpo degli articoli.");
                e.printStackTrace();
            }
            i++;
        }
        // Stampa il numero di articoli salvati con successo
        if (articlesBodyList.size() > 0) {
            System.out.println(articlesBodyList.size()+" articoli salvati correttamente.");
        }
    }

    /**
     * Ottiene il percorso della cartella in cui salvare gli articoli, creandola se non esiste.
     *
     * @param folderName Nome della cartella da ottenere o creare
     * @return Il percorso della cartella
     */
    public Path getFolder(String folderName) {
        Path folderPath = null;
        try {
            // Ottiene il percorso dal folderName
            folderPath = Paths.get(folderName);

            // Verifica se la cartella esiste già
            if (!Files.exists(folderPath)) {
                // Crea la cartella se non esiste
                Files.createDirectory(folderPath);
                System.out.println("Cartella " + folderName + " creata correttamente.");
            }
        } catch (Exception e) {
            System.out.println("Errore nella creazione della cartella.");
            e.printStackTrace();
        }
        return folderPath;
    }

}
