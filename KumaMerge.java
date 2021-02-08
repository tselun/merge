/* KumaMerge.java
* desc: pdf merge w/ gui
* author: stephen chiu
* date: jan 28, 2021
*/

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class KumaMerge extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L; // autofix

    // variables
    JButton openButton, saveButton;
    JTextArea info;
    JFileChooser chooser;
    FileNameExtensionFilter filter = new FileNameExtensionFilter("PDF Files", "pdf");
    ArrayList<String> paths = new ArrayList<String>();

    // construtor: main gui
    public KumaMerge() {
        super(new BorderLayout());

        // center: display info
        info = new JTextArea();
        info.setMargin(new Insets(5, 5, 5, 5));
        info.setEditable(false);
        JScrollPane center = new JScrollPane(info);

        // bottom: display buttons (file choosers)
        chooser = new JFileChooser();
        chooser.setFileFilter(filter);
        openButton = new JButton("open");
        openButton.setContentAreaFilled(false);
        openButton.addActionListener(this);
        saveButton = new JButton("save");
        saveButton.setContentAreaFilled(false);
        saveButton.addActionListener(this);
        JPanel bottom = new JPanel();
        bottom.add(openButton);
        bottom.add(saveButton);

        // main: defines layout of panel
        add(bottom, BorderLayout.PAGE_END);
        add(center, BorderLayout.CENTER);
    }

    // actions triggered by buttons
    public void actionPerformed(ActionEvent e) {

        // open: store pdfs
        if (e.getSource() == openButton) {
            int ret = chooser.showOpenDialog(KumaMerge.this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String fullPath = file.getAbsolutePath();
                info.append("opened: " + fullPath + "\n");
                paths.add(fullPath);
            } else {
                info.append("command cancelled \n");
            }
        }

        // save: merge pdfs
        else if (e.getSource() == saveButton) {
            info.append("pdfs merged, array cleared \n");
            try {
                merge(paths);
                paths.clear();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    // merge pdfs
    public void merge(ArrayList<String> paths) throws IOException {
        if (paths.size() > 0) {
            PDFMergerUtility merger = new PDFMergerUtility();
            for (String path : paths) {
                System.out.println("merging: " + path);
                merger.addSource(path);
            }
            merger.setDestinationFileName("out.pdf");
            merger.mergeDocuments(null);
            System.out.println("merge complete");
        } else {
            System.out.println("no pdfs specified!!");
        }
    }

    // entry point
    public static void main(String[] args) {
        JFrame frame = new JFrame("Kuma Gui");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new KumaMerge());
        frame.setVisible(true);
    }
}