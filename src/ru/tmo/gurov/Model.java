package ru.tmo.gurov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Model
{
    public static final int TC = 213;
    public static final int TS = 47;

    public static ArrayList<Transact> CEC;
    public static ArrayList<Transact> FEC;

    public static double TimeToChangeSpeed = 0;

    public static Random Gen = new Random();
    public static double CurrentTime = 0;
    public static int StartCount = 1;
    public static int channelsNum = 8;
    public static int TACount = 1;
    public static boolean MRFRLogic = false;
    public static int ModelingTime = 1480;

    public static Integer currentQueueLen = 0;
    public static Integer busyChannelsNum = 0;
    public static Map<Integer, Double> queueDict = new HashMap<>();
//    public static XYSeries series = new XYSeries("queue");
//    public static XYDataset queueDataset = new XYSeriesCollection();
    public static Map<Integer, Double> channelsDict = new HashMap<>();

    public static double Uniform(double a, double b)
    {
        return a + Gen.nextInt(1000) * (2*b - a) / 1000;
    }

    public static double Exponential(double M)
    {
        return Math.log(1 - Gen.nextDouble()) * -M;
    }

//    public static void PrintChain(ArrayList<Transact> chain, String name)
//    {
//        System.out.println(name);
//        for (Transact tr: chain
//        ) {
//            System.out.print(tr.toString());
//            System.out.print("\n");
//        }
//    }

//    public static void Graph(XYSeries series)
//    {
////        XYSeries series = new XYSeries("sin(a)");
//
//        for(float i = 0; i < Math.PI; i+=0.1){
//            series.add(i, Math.sin(i));
//        }
//
//        XYDataset xyDataset = new XYSeriesCollection(series);
//        JFreeChart chart = ChartFactory
//                .createXYLineChart("y = queue", "x", "y",
//                        xyDataset,
//                        PlotOrientation.VERTICAL,
//                        true, true, true);
//        JFrame frame =
//                new JFrame("MinimalStaticChart");
//        // Помещаем график на фрейм
//        frame.getContentPane()
//                .add(new ChartPanel(chart));
//        frame.setSize(400,300);
//        frame.show();
//    }

    public static void InsertPhase()
    {
        CEC = new ArrayList<>();
        FEC = new ArrayList<>();
        Gen = new Random();
        FEC.add(new Transact(TACount++, 0, 2));
        FEC.add(new Transact(TACount++, ModelingTime, 9));
    }

    public static void TimerCorrectionPhase()
    {
        for (Transact tr: FEC
             ) {
            switch (tr.GeneratorNum)
            {
                case (2):
                {
                    tr.RoadMark = 6;
                }
                case (3):
                {
                    tr.RoadMark = 6;
                }
                case (6):
                {
                    tr.RoadMark = 8;
                }
                case (9):
                {
                    tr.RoadMark = 10;
                }
            }
        }
        FEC.sort((transact, t1) -> -Double.compare(t1.TimeToNextPoint, transact.TimeToNextPoint));

        Transact first = new Transact(FEC.get(0));
        if (first.GeneratorNum == 2)
        {
//            FEC.add(new Transact(TACount++, first.TimeToNextPoint + Uniform(0, TS), 2));
            FEC.add(new Transact(TACount++, first.TimeToNextPoint + Exponential(TS), 2));
        }

        CEC.add(first);
        FEC.remove(0);
        ArrayList<Transact> tmp = new ArrayList<>();

        for (Transact tr: FEC
             ) {
            if(tr.TimeToNextPoint == first.TimeToNextPoint)
            {
                tmp.add(tr);
                CEC.add(new Transact(tr));
            }
        }

        for (Transact tr: tmp
             ) {
            FEC.remove(tr);
        }

        double dt = first.TimeToNextPoint - CurrentTime;

        if (queueDict.containsKey(currentQueueLen))
        {
            Double a = queueDict.get(currentQueueLen);
            a += dt;
//            series.add(currentQueueLen, a);
            queueDict.put(currentQueueLen, a);
//            System.out.println(series);
        }
        else
        {
            Double t = dt;
//            series.add(currentQueueLen, t);
            queueDict.put(currentQueueLen, dt);
        }

        if (channelsDict.containsKey(busyChannelsNum))
        {
            Double a = channelsDict.get(busyChannelsNum);
            a += dt;
            channelsDict.put(busyChannelsNum, a);
        }
        else
        {
            channelsDict.put(busyChannelsNum, dt);
        }

        if(!MRFRLogic)
            TimeToChangeSpeed += dt;

        CurrentTime = first.TimeToNextPoint;

        for (Transact tr: CEC
             ) {
            tr.TimeToNextPoint = first.TimeToNextPoint;
        }
    }
    
    public static void LookPhase()
    {
        boolean end = true;
        while (end)
        {
            end = false;
            ArrayList<Transact> tmp = new ArrayList<>(CEC);
            ArrayList<Transact> tmp2 = new ArrayList<>();

            tmp.sort((transact, t1) -> Double.compare(t1.GeneratorNum, transact.GeneratorNum));

            for (Transact tr: tmp
                 ) {
                switch (tr.GeneratorNum)
                {
                    case (2):
                    {
                        if (channelsNum == busyChannelsNum)
                        {
                            tr.GeneratorNum = 3;
                            currentQueueLen++;
                        }
                        else
                        {
                            tr.GeneratorNum = 6;
//                            tr.TimeToNextPoint += Uniform(0, TC);
                            tr.TimeToNextPoint += Exponential(TC);
                            busyChannelsNum++;

                            tmp2.add(tr);
                        }
                        break;
                    }

                    case (3):
                    {
                        if (busyChannelsNum < channelsNum)
                        {
                            tr.GeneratorNum = 6;
//                            tr.TimeToNextPoint += Uniform(0, TC);
                            tr.TimeToNextPoint += Exponential(TC);
                            currentQueueLen--;
                            busyChannelsNum++;

                            tmp2.add(tr);
                        }
                        break;
                    }

                    case (6):
                    {
                        tr.GeneratorNum = 8;
                        System.out.println(tr);
                        CEC.remove(tr);
                        busyChannelsNum--;
                        break;
                    }

                    case (9):
                    {
                        CEC.remove(tr);
                        System.out.println(tr);
                        StartCount--;
                        return;
                    }

                    default:
                        throw new IllegalStateException("Unexpected value: " + tr.GeneratorNum);
                }
            }

            FEC.addAll(tmp2);

            for (Transact tr: tmp2
                 ) {
                CEC.remove(tr);
            }
        }
    }
}

