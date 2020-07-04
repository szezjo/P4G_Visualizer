import java.io.*;
import java.awt.*;
import java.awt.image.*;
import java.util.Arrays;
import java.util.Observer;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Document;

public class Visualizer extends JComponent {

    static String chars_str = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz{|}           ĄąĆćĘęŁłŃńÓóŚśŹźŻż";
    int[] left_cuts = new int[chars_str.length()+1];
    int[] right_cuts = new int[chars_str.length()+1];

    protected String name="";
    protected String line1="";
    protected String line2="";
    protected String line3="";

    protected double scale=0.375;
    protected int normal_glyph_size=64;
    protected int glyph_size=(int)(normal_glyph_size*scale);

    protected int name_x=101;
    protected int name_y=748;
    protected int line_x=124;
    protected int line1_y=833;
    protected int line2_y=908;
    protected int line3_y=983;

    protected Image BackgroundImage;
    protected Image ScaledBackgroundImage;
    protected Image ScaledLetterSetAlpha;
    protected Image ScaledLetterSetAlphaNames;

    protected Editor editor;

    Visualizer(Editor parent) {
        this.editor=parent;
        this.loadCuts(new File("font0.xml"));
        this.BackgroundImage=parent.BackgroundImage;
        this.ScaledLetterSetAlphaNames=parent.ScaledLetterSetAlphaNames;
        this.ScaledLetterSetAlpha=parent.ScaledLetterSetAlpha;
        this.ScaledBackgroundImage=parent.ScaledBackgroundImage;

        JFrame f=new JFrame();
        f.getContentPane().setBackground(new Color(255,0,0));
        f.add(this);
        f.setSize((int)(1920*scale)+64,(int)(1080*scale)+64);
        f.setVisible(true);
    }

    public void loadCuts(File f) {
        int li=0;
        int ri=0;
        int len=chars_str.length();

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(f));
            String line = br.readLine();
            int i1=-1;
            int i2=-1;
            int tmp;
            double tf;

            while(line!=null && li<=len && ri<=len){
                i1=line.indexOf("<LeftCut>");
                if(i1!=-1) {
                    i2=line.indexOf("</LeftCut>");
                    if(i2!=-1){
                        tmp = Integer.parseInt(line.substring(i1+9,i2));
                        tf=tmp*scale;
                        left_cuts[li]=(int)tf;
                        li++;
                    }
                }
                else {
                    i1=line.indexOf("<RightCut>");
                    if(i1!=-1) {
                        i2=line.indexOf("</RightCut>");
                        if(i2!=-1){
                            tmp = Integer.parseInt(line.substring(i1+10,i2));
                            tf=tmp*scale;
                            right_cuts[ri]=(int)tf;
                            ri++;
                        }
                    }
                }
                line=br.readLine();
            }
            br.close();
        }
        catch (FileNotFoundException e) {return;}
        catch (IOException ie){System.exit(0);};

        left_cuts[0]=(int)(18*scale);
        right_cuts[0]=(int)(46*scale);
    }

    public void changeName(String line) {
        name=line;
    }

    public void changeLines(String lines) {
        lines = lines.replace("\n", "").replace("\r", "");
        int i=lines.indexOf("[n]");
        if(i==-1) {
            line1=lines;
            return;
        }
        line1=lines.substring(0,i);
        lines=lines.substring(i+3);
        i=lines.indexOf("[n]");
        if(i==-1) {
            line2=lines;
            return;
        }
        line2=lines.substring(0,i);
        lines=lines.substring(i+3);
        i=lines.indexOf("[n]");
        if(i==-1) {
            line3=lines;
            return;
        }
        line3=lines.substring(0,i);
    }


    public void refreshScale(double scale) {
        this.scale=scale;
        ScaledBackgroundImage = BackgroundImage.getScaledInstance((int)(1920*scale),(int)(1080*scale),Image.SCALE_FAST);
        ScaledLetterSetAlpha = editor.LetterSetAlpha.getScaledInstance((int)(editor.LetterSetAlpha.getWidth(this)*scale),(int)(editor.LetterSetAlpha.getHeight(this)*scale),Image.SCALE_SMOOTH);
        ScaledLetterSetAlphaNames = editor.LetterSetAlphaNames.getScaledInstance((int)(editor.LetterSetAlphaNames.getWidth(this)*scale),(int)(editor.LetterSetAlphaNames.getHeight(this)*scale),Image.SCALE_SMOOTH);
    }

    public void printChars(String toPrint, int sx, int sy, Graphics g, Image letterSet, ImageObserver o) {
        int x=(int)(sx*scale);
        int y=(int)(sy*scale);
        int cut=0;
        char c;
        int glyph;
        int row;
        int col;

        for (int it=0; it<toPrint.length(); it++) {
            c=toPrint.charAt(it);
            glyph=chars_str.indexOf(c);
            cut=right_cuts[glyph]-left_cuts[glyph];
            row=glyph/16;
            col=glyph%16;

            g.drawImage(letterSet,x,y,x+cut,y+glyph_size,(col*glyph_size)+left_cuts[glyph],row*glyph_size,(col*glyph_size)+right_cuts[glyph],(row+1)*glyph_size,o);
            x+=cut;
        }
    }

    public void paint(Graphics g) {
        g.drawImage(this.ScaledBackgroundImage,0,0,this);
        printChars(name,this.name_x,this.name_y,g,this.ScaledLetterSetAlphaNames,this);
        printChars(line1,this.line_x,this.line1_y,g,this.ScaledLetterSetAlpha,this);
        printChars(line2,this.line_x,this.line2_y,g,this.ScaledLetterSetAlpha,this);
        printChars(line3,this.line_x,this.line3_y,g,this.ScaledLetterSetAlpha,this);
    }
}
