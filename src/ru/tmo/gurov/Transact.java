package ru.tmo.gurov;

public class Transact
{
    public int TAnum;
    public double TimeToNextPoint;
    public int GeneratorNum;
    public int RoadMark;

    @Override
    public String toString() {
        return "Transact{" +
                "TAnum=" + TAnum +
                ", TimeToNextPoint=" + TimeToNextPoint +
                ", GeneratorNum=" + GeneratorNum +
                ", RoadMark=" + RoadMark +
                '}';
    }

    public Transact(int num, double startTime, int generator)
    {
        TAnum = num;
        TimeToNextPoint = startTime;
        GeneratorNum = generator;
        RoadMark = 0;
    }

    public Transact(Transact tr)
    {
        TAnum = tr.TAnum;
        TimeToNextPoint = tr.TimeToNextPoint;
        GeneratorNum = tr.GeneratorNum;
        RoadMark = tr.RoadMark;
    }
}

