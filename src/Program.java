import ru.tmo.gurov.Model;

public class Program
{
    static void Main(String[] args)
    {
        Model.InsertPhase();
        while (Model.StartCount != 0)
        {
            Model.TimerCorrectionPhase();
            Model.LookPhase();
        }
    }
}