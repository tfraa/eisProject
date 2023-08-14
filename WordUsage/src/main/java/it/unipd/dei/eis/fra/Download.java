package it.unipd.dei.eis.fra;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class Download{
    String folderName= "Articoli";
    
    protected abstract List<String> downloadArticles();
    protected void storeArticles(List<String> articlesBodyList, String folderName){
        int i = 1;
        Path p = getFolder(folderName);
        for(String body : articlesBodyList){
            File file = new File(p + "/Articolo" + i + ".txt");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter writer = new FileWriter(file);
                writer.write(body);
                writer.close();
            } catch (IOException e) {
                System.out.println("Error in writing body text to file");
                e.printStackTrace();
            }
            i++;
        }
        if (articlesBodyList.size() > 0) {
            System.out.println(articlesBodyList.size()+" articoli salvati correttamente.");
        }
    }

    public Path getFolder(String folderName) {
        Path folderPath = null;
        try {
            folderPath = Paths.get(folderName);
            if (!Files.exists(folderPath)) {
                Files.createDirectory(folderPath);
                System.out.println(folderName + " folder succesfully created.");
            }
        } catch (Exception e) {
            System.out.println("Error while handling folders");
            e.printStackTrace();
        }
        return folderPath;
    }

}
