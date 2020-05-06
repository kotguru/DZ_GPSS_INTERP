package ru.tmo.gurov;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Model
{
    public static ArrayList<Transact> CEC;
    public static ArrayList<Transact> FEC;

    public static int AQMRCheckCount = 0;
    public static double ATinQMR = 0;
    public static int AEinQMR = 0;

    public static double TimeToChangeSpeed = 0;

    public static Random Gen;
    public static double CurrentTime = 0;
    public static int StartCount = 1;
    public static int TACount = 1;
    public static boolean MRFRLogic = false;
    public static int ModelingTime = 3600;

    public static double Uniform(double a, double b)
    {
        return a + Gen.nextInt(1000) * (b - a) / 1000;
    }

    public static void InsertPhase()
    {
        CEC = new ArrayList<Transact>();
        FEC = new ArrayList<Transact>();
        Gen = new Random();
        FEC.add(new Transact(TACount++, 0, 1));
        FEC.add(new Transact(TACount++, Uniform(0, 3), 2));
        FEC.add(new Transact(TACount++, Uniform(0, 5), 3));
        FEC.add(new Transact(TACount++, ModelingTime, 4));
    }

    public static void TimerCorrectionPhase()
    {
        FEC.sort(new Comparator<Transact>() {
            @Override
            public int compare(Transact transact, Transact t1) {
                return Double.compare(t1.TimeToNextPoint, transact.TimeToNextPoint);
            }
        });

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

        double dt = first.TimeToNextPoint - CurrentTime;

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
            ArrayList<Transact> tmp = new ArrayList<Transact>(CEC);

            for (Transact tr: tmp
                 ) {
                switch (tr.GeneratorNum)
                {

                    case (4):
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

