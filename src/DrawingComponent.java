import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import static java.awt.Color.RED;

class DrawingComponent extends JPanel {
    @Override
    protected void paintComponent(Graphics gh) {
        Graphics2D drp = (Graphics2D)gh;

        //горизонтальные линии и обозначения
        for (int i=0; i<11; i++) {
            drp.drawLine(50, 50+44*i, 490, 50+44*i);
            int vs = 100 - i*10;
            drp.drawString(vs+"%", 30, 50+44*i);
        }

        drp.drawString("queue", 100, 40);
        for (int i = 0; i < 20; i++)
        {
            drp.drawString(i + "", 60 + i*20, 510);
//            drp.drawString("Февраль", 160, 420);
//            drp.drawString("Март", 260, 420);
        }

        drp.setColor(Color.BLUE);
        drp.fillRect(80, 30, 10, 10);
//        drp.setColor(RED);
//        drp.fillRect(80, 50, 10, 10);
//        drp.setColor(Color.GREEN);
//        drp.fillRect(80, 70, 10, 10);

        for (int i = 0; i< Array.getLength(GrGis.y); i++) {
            //строим саму гистограмму 
            //извлекаем цвет для каждого графика
            Color color = RED;
            for (int j=0;j<1;j++) {
                try {
                    Field field = Class.forName("java.awt.Color").getField(GrGis.col[j].toLowerCase());
                    color = (Color)field.get(null);
                } catch (Exception e) {}
                drp.setColor(color);
                //переводим полученные данные в реальные координаты  
                int realY = (int) (490-44*GrGis.y[i]/10)+3;
                drp.fillRect(50+20*j+20*i, realY, 15,(int) (GrGis.y[i]*4.4));
            }
        }
    }
}