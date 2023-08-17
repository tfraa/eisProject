package it.unipd.dei.eis.fra;
import edu.stanford.nlp.simple.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
    Classe che legge i file txt di una determinata directory, e conta le parole più frequenti.
 */
public class Analyze{

    /**
     * Estrae le parole più frequenti dai file di testo presenti in una determinata cartella.
     *
     * @param folderName Il percorso della cartella contenente i file di testo da analizzare.
     */
    public void extractTerms(String folderName){
        // Set di stringhe contenente le parole da non considerare
        Set<String> EXCLUDED_WORDS = new HashSet<>();
        try {
            // Legge le parole vietate dal file "BanList.txt" e le aggiunge al Set
            FileReader fl = new FileReader("BanList.txt");
            BufferedReader br = new BufferedReader(fl);
            String line;
            while ((line = br.readLine()) != null) {
                EXCLUDED_WORDS.add(line);
            }
        }catch(Exception e){
            System.out.println("Errore nel caricamento della stoplist.");
        }

        List<Path> filePaths = getTxtFilePaths(folderName);
        if (filePaths.isEmpty()) {
            System.out.println("Errore. La cartella potrebbe non ancora essere stata creata.");
            return;
        }

        // Mappa per conteggiare le parole
        Map<String, Integer> wordCounts = new HashMap<>();

        // Set per tenere traccia delle parole già conteggiate
        Set<String> countedWords = new HashSet<>();

        // Lista di stringhe per ogni file nella cartella
        List<String> lines = null;

        for (Path filePath : filePaths) {
            Set<String> uniqueWords = new HashSet<>();
            try{
                // Legge tutte le righe dal file di testo
                lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            } catch(IOException e){
                System.out.println("Errore nella lettura del file");
                e.printStackTrace();
            }
            for (String line : lines) {
                List<String> words = getWords(line);
                for (String word : words) {
                    // Mette in minuscolo la parola, verifica se è esclusa e la aggiunge all'insieme delle parole uniches
                    String cleanWord = word.toLowerCase();
                    if (!isExcludedWord(cleanWord, EXCLUDED_WORDS)) {
                        uniqueWords.add(cleanWord);
                    }
                }
            }
            for (String word : uniqueWords) {
                if (countedWords.contains(word)) {
                    // Aggiorna il conteggio delle parole
                    Integer count = wordCounts.get(word);
                    if (count == null) {
                        count = 1;
                    }
                    wordCounts.put(word, count + 1);
                } else {
                    countedWords.add(word);
                    wordCounts.put(word, 1);
                }
            }
        }

        // Ordina le parole per quantità di apparizioni e stampa le prime 10 parole più frequenti
        List<Map.Entry<String, Integer>> sortedEntries = sortByValue(wordCounts);
        System.out.println("Le parole più frequenti sono:");
        int count = 0;
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            if (count >= 10) {
                break;
            }
            System.out.println(entry.getKey() + " - " + entry.getValue());
            count++;
        }

        // Salva i risultati nel file
        storeResults(sortedEntries);
    }

    /**
     * Salva i risultati delle parole più frequenti in un file di testo.
     *
     * @param sortedEntries Una lista ordinata di coppie (parola, frequenza).
     */
    private void storeResults(List<Map.Entry<String, Integer>> sortedEntries){
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("parolefrequenti.txt"));
            writer.write("Le 10 parole più frequenti sono: \n");
            int count = 0;
            for (Map.Entry<String, Integer> entry : sortedEntries){
                if (count >= 10) {break;}
                writer.write(entry.getKey() + " - " + entry.getValue()+"\n");
                count++;
            }
            writer.close();
        }catch(IOException e){
            System.out.println("Errore nel salvataggio dei risultati nel file txt");
        }
    }

    /**
     * Restituisce una lista contenente i titoli dei file txt presenti nella directory specificata.
     *
     * @param folderPath Il percorso della directory da cui estrarre i titoli dei file TXT.
     * @return Una lista di stringhe che rappresentano i titoli dei file TXT trovati nella directory.
     */
    private static List<Path> getTxtFilePaths(String folderPath){
        List<Path> filePaths = new ArrayList<>();
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                    filePaths.add(file.toPath());
                }
            }
        }
        return filePaths;
    }

    /**
     * Estrae le singole parole da un testo dato usando CoreNLP e le restituisce come una lista di stringhe.
     *
     * @see Sentence
     * @param text Il testo da cui estrarre le parole.
     * @return Una lista contenente le singole parole estratte dal testo.
     */
    private static List<String> getWords(String text) {
        Sentence sentence = new Sentence(text);
        return sentence.words();
    }

    /**
     * Verifica se una parola è presente nel set di parole escluse.
     *
     * @param word La parola da verificare.
     * @param EXCLUDED_WORDS Il set di parole escluse da confrontare.
     * @return true se la parola è presente nel set di parole escluse, false altrimenti.
     */
    private static boolean isExcludedWord(String word, Set<String> EXCLUDED_WORDS) {
        return EXCLUDED_WORDS.contains(word);
    }

    /**
     * Ordina una mappa in base ai valori dei suoi elementi e restituisce una lista
     * di voci mappa (chiave-valore) ordinate in ordine decrescente dei valori.
     *
     * @param map La mappa da ordinare in base ai valori.
     * @return Una lista di voci mappa (chiave-valore) ordinate per valore in ordine decrescente.
     */
    private static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }
        });
        return entries;
    }
}