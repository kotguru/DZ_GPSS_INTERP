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

        Model.Graph(Model.series);
    }
}