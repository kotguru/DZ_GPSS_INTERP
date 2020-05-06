package ru.tmo.gurov;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Model
{
    public static ArrayList<Transact> CEC;
    public static ArrayList<Transact> FEC;

    public static double TimeToChangeSpeed = 0;

    public static Random Gen = new Random();
    public static double CurrentTime = 0;
    public static int StartCount = 1;
    public static int ChannelsNum = 5;
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
        FEC.add(new Transact(TACount++, CurrentTime + Uniform(0, 94), 2 ));

        FEC.sort(new Comparator<Transact>() {
            @Override
            public int compare(Transact transact, Transact t1) {
                return -Double.compare(t1.TimeToNextPoint, transact.TimeToNextPoint);
            }
        });

//        PrintChain(FEC);

        Transact first = new Transact(FEC.get(0));
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
            else
            {
                break;
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

            System.out.println("_tmp_");
            PrintChain(tmp);
            for (Transact tr: tmp
                 ) {
                switch (tr.GeneratorNum)
                {
                    case (2):
                    {

                        break;
                    }

                    case (3):
                    {
                        break;
                    }

                    case (4):
                    {
                        break;
                    }

                    case (5):
                    {
                        break;
                    }

                    case (6):
                    {
                        break;
                    }

                    case (7):
                    {
                        break;
                    }

                    case (8):
                    {
                        break;
                    }

                    case (9):
                    {
                        CEC.remove(tr);
                        StartCount -= 1;
                        return;
                    }
                    default:
                        throw new IllegalStateException("Unexpected value: " + tr.GeneratorNum);
                }
            }
        }
    }
}

