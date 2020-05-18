package uz.texnoblog.texnoblogsite;

public class Token {
    public static String u_token;
    /**
     * @param token
     */
    public static void setToken(String token){
        u_token = token;
    }

    protected final static String getToken(){
        return u_token;
    }
}
