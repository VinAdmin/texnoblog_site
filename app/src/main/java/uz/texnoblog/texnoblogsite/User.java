package uz.texnoblog.texnoblogsite;

public class User {
    public String id;
    private static String token;
    public static String success = "NO";

    public void setTokent(String token){
        User.token = token;
        return;
    }

    public String getToken(){
        return User.token;
    }
}
