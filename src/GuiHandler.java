import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

public class GuiHandler extends JFrame {
    JPanel textFilePanel, stopWordFilePanel, centralPanel = new JPanel();
    FileGrabber textFileGrabber = new FileGrabber(), stopWordFileGrabber = new FileGrabber();
    TreeMap<String, Integer> keyWords = new TreeMap<>();
    JTextArea textArea = new JTextArea();

    public GuiHandler() {
        super("Text Analyzer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);

        centralPanel.add(textFilePanel = CreateFileInputArea(
                textFileGrabber,
                new JButton("Select File"),
                new JTextField(),
                "Select File To Analyze"
        ));

        centralPanel.add(stopWordFilePanel = CreateFileInputArea(
                stopWordFileGrabber,
                new JButton("Select File"),
                new JTextField(),
                "Select Stop Words File"
        ));

        JButton goButton = new JButton("Analyze");
        goButton.addActionListener(e -> {OnAnalyze();});

        JButton saveButton = new JButton("Save Analysis");
        saveButton.addActionListener(e -> {OnSave();});


        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        centralPanel.add(goButton);
        centralPanel.add(saveButton);
        centralPanel.add(scrollPane);
        textArea.setEditable(false);

        this.add(centralPanel);
    }

    private void OnAnalyze(){
        textArea.setText("");
        if(textFileGrabber.GetFile() == null || stopWordFileGrabber.GetFile() == null){
            JOptionPane.showMessageDialog(null, "Please select a file!");
        }
        else{
            keyWords = textFileGrabber.ReadFileKeyWords(stopWordFileGrabber.ReadFileAsStopWords());
            for(String key : keyWords.keySet()){
                textArea.append(key + " " + keyWords.get(key) + "\n");
            }
        }
    }
    private void OnSave(){
        if(keyWords.size() > 0){
            FileSaver fileSaver = new FileSaver();
            fileSaver.SaveFile(keyWords);
        }
        else{
            JOptionPane.showMessageDialog(null, "No analysis to save!");
        }
    }

    private JPanel CreateFileInputArea(FileGrabber fileGrabber,JButton button, JTextField textField, String label){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        textField.setEditable(false);
        textField.setPreferredSize(new Dimension(500, 30));

        button.addActionListener(e -> {
            fileGrabber.GetFileFromUser();
            SwingUtilities.invokeLater(() -> {//wait for the file to be read
                textField.setText(fileGrabber.GetFilePath());
            });
            panel.revalidate();
            panel.repaint();
        });

        JLabel label1 = new JLabel(label);
        panel.add(label1);

        panel.add(button);
        panel.add(textField);

        return panel;
    }
}
