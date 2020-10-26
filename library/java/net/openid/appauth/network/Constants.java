package net.openid.appauth.network;

/**
 * Created by Jitendra on 22,March,2019
 */

public class Constants {

    public static String BASE_URL_LIVE = "https://app.asana.com";

    public static enum API_TYPE {
        AUTH_CODE("AUTH_CODE"),
        INITIAL_TOKEN("INITIAL_TOKEN"),

        GET_PROJECTS("GET_PROJECTS"),
        GET_TEAMS("GET_TEAMS"),
        GET_USER_DETAILS("GET_USER_DETAILS"),
        GET_TEAMS_IN_ORGANIZATION("GET_TEAMS_IN_ORGANIZATION"),
        GET_TEAMS_FOR_USER("GET_TEAMS_FOR_USER"),

        REFRESH_TOKEN("REFRESH_TOKEN");







        private String url;

        API_TYPE(String url) {
            this.url = url;
        }

        public String url() {
            return url;
        }
    }

    public static String CLIENT_ID = "1189021352957129";
    public static String CLIENT_SECRET = "cfb17d828cce7f46ddb6891ac9ad07f1";
    public static String REDIRECT_URI = "https://www.pqi.org/app/AppAuth.html";
    public static String PERSONAL_ACCESS_TOKEN = "1/1175663495251439:6151ba2ed10b6d086d4235eb6615e8bc";
}
