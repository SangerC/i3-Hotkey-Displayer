package i3HotKeyDisplay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class HotKeyDisplay {

    File i3Config;
    File displayConfig;
    String string;
    JTextArea resultArea;
    JTextField searchBar;

    public HotKeyDisplay(String i3Config, String displayConfig) {
        this.i3Config = new File(i3Config);
        this.displayConfig = new File(displayConfig);
        this.parse();
    }

    private void parse() {
        FileReader file = null;
        try {
            file = new FileReader(i3Config);
        } catch (FileNotFoundException e) {
            System.out.println("check");
            System.exit(0);
        }
        this.string = "";
        Scanner s = new Scanner(file);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if ((line.contains("bindsym") || line.contains("bindcode")) && !line.contains("#")) {
                this.string += line;
                this.string += "\n";
            }
        }
        s.close();
        try {
            file = new FileReader(displayConfig);
        } catch (FileNotFoundException e) {
            s.close();
            return;
        }
        s = new Scanner(file);
        while (s.hasNextLine()) {
            String input = s.nextLine();
            String value = beforeMarker(input, ':', true);
            String replacement = beforeMarker(input, ':', false);
            this.string = this.string.replaceAll(value, replacement);
        }
        s.close();
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String beforeMarker(String input, char marker, boolean before) {
        String output = input;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == marker) {
                if (before) {
                    output = input.substring(0, i);
                } else {
                    output = input.substring(i + 1, input.length());
                }
            }
        }
        return output;
    }

    public void draw(){
        JFrame frame = new JFrame();
        frame.setSize(1280,720);
        frame.setTitle("i3HotkeyHelper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setBackground(new Color(34, 45, 50));
        this.resultArea = new JTextArea();
        this.searchBar = new JTextField();
        searchBar.addActionListener(new TextChangeListener(this));
        frame.add(this.searchBar,BorderLayout.NORTH);
        frame.add(this.resultArea,BorderLayout.CENTER);
        this.resultArea.setText(this.string);
        this.resultArea.setForeground(new Color(0, 188, 212));
        JScrollPane scrollPane = new JScrollPane(this.resultArea);
        scrollPane.setBounds(10,60,780,500);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane,BorderLayout.CENTER);
        this.resultArea.setBackground(new Color(34, 45, 50));
        frame.revalidate();
        frame.repaint();
    }

    public class TextChangeListener implements ActionListener {

        private HotKeyDisplay hotKeyDisplay;

        public TextChangeListener(HotKeyDisplay hotKeyDisplay){
            this.hotKeyDisplay=hotKeyDisplay;
        }

        @Override
        public void actionPerformed(ActionEvent arg0){
            this.hotKeyDisplay.getResultArea().setText(searchFor(this.hotKeyDisplay.getString(), this.hotKeyDisplay.getSearchBar().getText()));
        }

    }

    public String searchFor(String input, String term){
        String output = "";
 
        Scanner s = new Scanner(input);
        while(s.hasNextLine()){
            String line = s.nextLine();
            if(line.contains(term)){
                output+=line;
                output+="\n";
            }  
        }
        s.close();
        return output;
    }
    public JTextArea getResultArea() {
        return resultArea;
    }

    public String getString() {
        return string;
    }

    public JTextField getSearchBar() {
        return searchBar;
    }
}

