package gal.etse.ense.aventurasubmarina.Utils;

public class DebugPrint {
    static boolean debuggeando=true;

    public static void show(String s){
        if(debuggeando){
            System.out.println(s);
        }
    }

}
