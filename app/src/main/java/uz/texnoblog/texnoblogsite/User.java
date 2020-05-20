package uz.texnoblog.texnoblogsite;

public class User {
    private static String token;
    public static String success = "NO";

    public void setTokent(String token){
        User.token = token;
        return;
    }

    public String getToken(){
        if(User.token.length() != 0) {
            return User.token ;
        }
        else{
            return null;
        }
    }
}
