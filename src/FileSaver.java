import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.TreeMap;

public class FileSaver {
    JFileChooser fileChooser = new JFileChooser();
    File savedFile;

    public void SaveFile(TreeMap<String, Integer> keyWords){
        boolean hasValidFile = false;

        File workingDirectory = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(workingDirectory);//set the current directory for the file chooser

        while(!hasValidFile){
            fileChooser.showSaveDialog(null);
            savedFile = fileChooser.getSelectedFile();
            if(savedFile.exists()){

                int option = JOptionPane.showConfirmDialog(null,
                        "File already exists. Do you want to overwrite it?",
                        "File Already Exists", JOptionPane.YES_NO_OPTION
                );

                if(option == JOptionPane.NO_OPTION){
                    savedFile = null;//if they say no, set the file to null and repeat the loop
                }
                else{
                    hasValidFile = true;//if they say yes, set the file to the selected file and close the loop
                }
            }
            else{
                hasValidFile = true;//if the file doesn't exist, set the file to the selected file and close the loop
            }
        }

        if(savedFile != null && !savedFile.getAbsolutePath().endsWith(".txt")){
            savedFile = new File(savedFile.getAbsolutePath() + ".txt");
        }

        try(FileWriter writer = new FileWriter(savedFile)){
            for(String key : keyWords.keySet()){
                writer.write(key + " " + keyWords.get(key) + "\n");
            }
            JOptionPane.showMessageDialog(null, "File saved!");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Error saving file!");
            e.printStackTrace();
        }
    }
}
