import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;

public class Editor {
    protected Image BackgroundImage;
    protected Image ScaledBackgroundImage;
    protected BufferedImage OriginalLetterSet;
    protected Image LetterSetAlpha;
    protected Image LetterSetAlphaNames;
    protected Image ScaledLetterSetAlpha;
    protected Image ScaledLetterSetAlphaNames;

    protected double scale;

    protected Image TransformBlacktoAlpha(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                final Color filterColor = new Color(rgb);
                final int r = filterColor.getRed();
                final int b = filterColor.getBlue();
                final int g = filterColor.getGreen();
                if (r>=0 && r<=128 && b>=0 && b<=128 && g>=0 && g<=128) return 0;
                else return rgb;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    protected Image TransformBlacktoAlphaName(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(int x, int y, int rgb) {
                final Color filterColor = new Color(rgb);
                final Color nameColor = new Color(76,40,24);
                final int r = filterColor.getRed();
                final int b = filterColor.getBlue();
                final int g = filterColor.getGreen();
                if (r>=0 && r<=128 && b>=0 && b<=128 && g>=0 && g<=128) return 0;
                else return nameColor.getRGB();
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public void loadImages() {
        try {
            BackgroundImage = ImageIO.read(new File("bg.jpg"));
            OriginalLetterSet= ImageIO.read(new File("font0.png"));
            LetterSetAlpha=TransformBlacktoAlpha(OriginalLetterSet);
            LetterSetAlphaNames=TransformBlacktoAlphaName(OriginalLetterSet);
        }
        catch (Exception e) {};
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        EditorUI ui = new EditorUI();
        f.setContentPane(ui.mainPanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        f.setSize(400,200);

        Editor editor = new Editor();
        editor.loadImages();
        Visualizer v = new Visualizer(editor);
        v.refreshScale(0.375);

        ui.originalNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document d = ui.originalNameField.getDocument();
                try {
                    v.changeName(d.getText(0,d.getLength()));
                    v.repaint();
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document d = ui.originalNameField.getDocument();
                try {
                    v.changeName(d.getText(0,d.getLength()));
                    v.repaint();
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Document d = ui.originalNameField.getDocument();
                try {
                    v.changeName(d.getText(0,d.getLength()));
                    v.repaint();
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }
        });

        ui.originalTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Document d = ui.originalTextArea.getDocument();
                try {
                    v.changeLines(d.getText(0,d.getLength()));
                    v.repaint();
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Document d = ui.originalTextArea.getDocument();
                try {
                    v.changeLines(d.getText(0,d.getLength()));
                    v.repaint();
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Document d = ui.originalTextArea.getDocument();
                try {
                    v.changeLines(d.getText(0,d.getLength()));
                    v.repaint();
                } catch (BadLocationException badLocationException) {
                    badLocationException.printStackTrace();
                }
            }
        });
    }
}
