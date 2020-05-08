import ru.tmo.gurov.Model;

public class Program
{
    public static void main(String[] args)
    {
        Model.InsertPhase();
        while (Model.StartCount != 0)
        {
            Model.TimerCorrectionPhase();
            Model.LookPhase();
        }

//        Model.Graph(Model.series);
//        System.out.println(Model.queueDict);
//        System.out.println(Model.channelsDict);
        for (int i = 0; i < 20; i++)
        {
            Double a = Model.queueDict.getOrDefault(i, 0.0);
            a /= Model.ModelingTime;
            Model.queueDict.put(i, a*100);

            Double b = Model.channelsDict.getOrDefault(i, 0.0);
            b /= Model.ModelingTime;
            Model.channelsDict.put(i, b*100);
        }
//        System.out.println(Model.queueDict);
//        System.out.println(Model.channelsDict);
        GrGis gr = new GrGis (Model.channelsDict, Model.queueDict);
        gr.setVisible(true);
//        while(gr.isFocusable())
//        {
//
//        }
//        System.out.println("___________________________1___________________________");
//        GrGis gr2 = new GrGis (Model.channelsDict);
        for (int i = 0; i < 20; i++)
        {
            System.out.println("queueDict.put(" + i + "," + Model.queueDict.getOrDefault(i, 0.0) + ");");
        }

        for (int i = 0; i < 20; i++)
        {
            System.out.println("channelsDist.put(" + i + "," + Model.channelsDict.getOrDefault(i, 0.0) + ");");
        }
//        gr2.setVisible(true);
    }
}