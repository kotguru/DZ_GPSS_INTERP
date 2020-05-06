package ru.tmo.gurov;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Model
{
    public static final int TC = 426;
    public static final int TS = 94;

    public static ArrayList<Transact> CEC;
    public static ArrayList<Transact> FEC;

    public static double TimeToChangeSpeed = 0;

    public static Random Gen = new Random();
    public static double CurrentTime = 0;
    public static int StartCount = 1;
    public static int channelsNum = 2;
    public static int busyChannelsNum = 0;
    public static int TACount = 1;
    public static boolean MRFRLogic = false;
    public static int ModelingTime = 480;

    public static double Uniform(double a, double b)
    {
        return a + Gen.nextInt(1000) * (b - a) / 1000;
    }

//    public static double Exponential(double mean)
//    {
//        Random random = new Random();
//        return Math.log()
//    }

    public static void PrintChain(ArrayList<Transact> chain)
    {
        System.out.println("__New_chain__");
        for (Transact tr: chain
        ) {
            System.out.print(tr.toString());
            System.out.print("\n");
        }
    }

    public static void InsertPhase()
    {
        CEC = new ArrayList<Transact>();
        FEC = new ArrayList<Transact>();
        Gen = new Random();
        FEC.add(new Transact(TACount++, 0, 2));
        FEC.add(new Transact(TACount++, ModelingTime, 9));
//        PrintChain(CEC);
//        PrintChain(FEC);
    }

    public static void TimerCorrectionPhase()
    {
        FEC.sort(new Comparator<Transact>() {
            @Override
            public int compare(Transact transact, Transact t1) {
                return -Double.compare(t1.TimeToNextPoint, transact.TimeToNextPoint);
            }
        });

//        PrintChain(FEC);

        Transact first = new Transact(FEC.get(0));
        if (first.GeneratorNum == 2)
        {
            FEC.add(new Transact(TACount++, first.TimeToNextPoint + Uniform(0, TS), 2));
        }
        CEC.add(first);
        FEC.remove(0);
        ArrayList<Transact> tmp = new ArrayList<Transact>();

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

//        PrintChain(CEC);
//        PrintChain(FEC);

        double dt = first.TimeToNextPoint - CurrentTime;

        if(!MRFRLogic)
            TimeToChangeSpeed += dt;

//        System.out.println(TimeToChangeSpeed);
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
            ArrayList<Transact> tmp = new ArrayList<Transact>(CEC);
            ArrayList<Transact> tmp2 = new ArrayList<Transact>();

            tmp.sort(new Comparator<Transact>() {
                @Override
                public int compare(Transact transact, Transact t1) {
                    return Double.compare(t1.GeneratorNum, transact.GeneratorNum);
                }
            });

            PrintChain(tmp);
            for (Transact tr: tmp
                 ) {
                switch (tr.GeneratorNum)
                {
                    case (2):
                    {
                        if (channelsNum == busyChannelsNum)
                        {
                            tr.GeneratorNum = 3;
                        }
                        else
                        {
                            tr.GeneratorNum = 6;
                            tr.TimeToNextPoint += Uniform(0, TC);
                            busyChannelsNum++;

                            tmp2.add(tr);
//                            tmp.remove(tr);
                        }
                        break;
                    }

                    case (3):
                    {
                        if (busyChannelsNum < channelsNum)
                        {
                            tr.GeneratorNum = 6;
                            tr.TimeToNextPoint += Uniform(0, TC);
                            busyChannelsNum++;

                            tmp2.add(tr);
//                            tmp.remove(tr);
                        }
                        break;
                    }

                    case (6):
                    {
                        tr.GeneratorNum = 8;
                        CEC.remove(tr);
//                        tmp.remove(tr);
                        busyChannelsNum--;
                        break;
                    }

                    case (9):
                    {
                        CEC.remove(tr);
                        StartCount --;
                        return;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + tr.GeneratorNum);
                }
            }

            for (Transact tr: tmp2
                 ) {
                FEC.add(tr);
            }

            for (Transact tr: tmp2
                 ) {
                CEC.remove(tr);
            }
//            CEC = tmp;
        }
    }
}

