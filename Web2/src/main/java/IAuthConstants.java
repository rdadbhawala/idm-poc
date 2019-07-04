public interface IAuthConstants
{
    static final String prmAction = "act";
    static final String hdrToken = "X-Syncope-Token";

    static final String actLogin = "login";
    
    static final String prmUser = "usr";
    static final String prmPassword = "pwd";

    static final String actLogout = "logout";
    
    static final String actVerify = "verify";

	static final String urlUsersIdm = "http://localhost:8888/Users/idm";
	static final String hdrContextPath = "CONTEXT-PATH";
}
