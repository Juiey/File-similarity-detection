import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
public class fileSimilarityDetectorGUI {
    private static FileDialog openDia;
    private static final String TITLE = "文件相似度检测";
    private static JTextArea file1PathTextField;

    private static JTextArea file2PathTextField;
    private static JLabel similarityLabel;
    private static String similarityLabelString;



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createGUI());
    }

    private static void createGUI() {
        JFrame frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 550);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayout(1, 2));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(2, 1));

        openDia = new FileDialog(frame, "选择", java.awt.FileDialog.LOAD);
        JButton selectFile1Button = new JButton("选择文件 1");
        selectFile1Button.setPreferredSize(new Dimension(350, 50));
        selectFile1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = selectFilePath();
                if (filePath != null) {
                    file1PathTextField.setText(filePath);
                }
            }
        });
        panel.add(selectFile1Button);

        JButton selectFile2Button = new JButton("选择文件 2");
        selectFile2Button.setPreferredSize(new Dimension(350, 50));
        selectFile2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = selectFilePath();
                if (filePath != null) {
                    file2PathTextField.setText(filePath);
                }
            }
        });
        panel.add(selectFile2Button);


        file1PathTextField = new JTextArea(6,40);
        file1PathTextField.setEditable(false);
        file1PathTextField.setLineWrap(true); // 开启自动换行功能
        JScrollPane scrollPane = new JScrollPane(file1PathTextField);
        file1PathTextField.setText("文件1路径");
        panel1.add(scrollPane);



        file2PathTextField = new JTextArea(6,40);
        file2PathTextField.setEditable(false);
        file2PathTextField.setLineWrap(true); // 开启自动换行功能
        JScrollPane scrollPane1 = new JScrollPane(file2PathTextField);
        file2PathTextField.setText("文件2路径");
        panel1.add(scrollPane1);

        JButton calculateSimilarityButton = new JButton("计算相似度");
        calculateSimilarityButton.setPreferredSize(new Dimension(100, 30));

        calculateSimilarityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String file1Path = file1PathTextField.getText();
                    String file2Path = file2PathTextField.getText();

                    double similarity = calculateSimilarity(file1Path, file2Path);
                    similarityLabel.setText(similarityLabelString+"    余弦相似度:" + similarity);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel2.add(calculateSimilarityButton);

        similarityLabel = new JLabel();
        similarityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        similarityLabel.setText("目前仅支持word文档类型检测");

        panel2.add(similarityLabel);

      //  frame.add(similarityLabel, BorderLayout.CENTER);

        frame.add(panel, BorderLayout.NORTH);

        frame.add(panel1, BorderLayout.CENTER);
        frame.add(panel2, BorderLayout.SOUTH);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private static String selectFilePath() {
        openDia.setVisible(true);//显示打开文件对话框
        String dirpath = openDia.getDirectory();//获取打开文件路径并保存到dirpath中
        String fileName = openDia.getFile();//获取打开文件名称并保存到fileName中

        if (dirpath == null || fileName == null)//判断路径和文件是否为空
            return null;
        else return dirpath+fileName;
    }

    private static String getFileContent(String filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] data = new byte[(int) fileInputStream.available()];
        fileInputStream.read(data);
        fileInputStream.close();
        return new String(data);
    }
    private static double calculateSimilarity(String file1Path, String file2Path) throws IOException {

        int lastDotIndex = file1Path.lastIndexOf('.');
        String a = file1Path.substring(lastDotIndex + 1);
        System.out.println(a);
        
        String file1Content = "";
        String file2Content = "";

        if (a.equals("txt")) {
            file1Content = getFileContent(file1Path);
            file2Content = getFileContent(file2Path);
        }else {
        try {
            // 指定要读取的 WPS Word 文档路径
            FileInputStream fis = new FileInputStream(file1Path);
            FileInputStream fis2 = new FileInputStream(file2Path);

            // 创建 XWPFDocument 对象表示文档
            XWPFDocument document = new XWPFDocument(fis);
            XWPFDocument document2 = new XWPFDocument(fis2);


            // 遍历段落并输出内容
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                file1Content += paragraph.getText();

            }
            System.out.println("-------------------------------------------------------");
            for (XWPFParagraph paragraph : document2.getParagraphs()) {
                file2Content += paragraph.getText();
            }
            // 关闭文件流
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        }

        int lcs = ld_lcs_algorithm.getLCS(file1Content, file2Content).length();
        int ld = ld_lcs_algorithm.ld(file1Content, file2Content);
        float sim = (float)(lcs) / (ld+lcs);
        System.out.println("Similarity degree:" + sim);
        similarityLabelString = "最长公共子序列相似度:" + sim+"\n";

       // double score = CosineSimilarity.getSimilarity(file1Content, file2Content);
        System.out.println("``````````````````````````````````````````````````````");
      //  similarityLabelString += "   余弦相似度(HanLP中文自然语言标准分词):" + score +"\n";

        file1PathTextField.setText(file1Content+"\n---------------------------------------------");
        file2PathTextField.setText(file2Content+"\n---------------------------------------------");
        double similarity = FileSimilarityInCosine.calculate(file1Content, file2Content);
        return similarity;
    }


}
