import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GrGis extends JFrame {
    public static double y[] = new double[20];
    public static String col[] = {"BLUE", "RED"};//массив цветов

    public GrGis(Map<Integer, Double> queueDict, Map<Integer, Double> channelsDict) {
        super("Обычная гистограмма");
        JPanel jcp = new JPanel(new BorderLayout());
        setContentPane(jcp);
        jcp.add(new DrawingComponent(), BorderLayout.CENTER);
        jcp.setBackground(Color.gray);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (int i = 0; i < 20; i++) {
            y[i] = queueDict.getOrDefault(i, 0.0);

//            y[i][1] = channelsDict.getOrDefault(i, 0.0);
//            System.out.println(y[i]);
        }

//    public static void main(String[] args) {
//        y[0]=5;
//        y[1]=24;
//        y[2]=20;
//
//        y[3]=24;
//        y[4]=20;
//
//        GrGis gr = new GrGis ();
//        gr.setVisible(true);
//    }
    }
}