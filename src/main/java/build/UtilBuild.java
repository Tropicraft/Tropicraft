package build;

public class UtilBuild {

    public static boolean debug = true;

    public static void dbg(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

}
