import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilterSyncope 
        extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("doFilter");
        Cookie found = getAuthCookie(req);
        if (found != null) {
            System.out.println("Cookie Value: " + found.getValue());
            new IdmActionsSyncope().verify(found.getValue());
            super.doFilter(req, res, chain);
        } else {
            res.getWriter().write("IDM FAILURE");
        }
    }


    // @Override
    // public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    //         throws IOException, ServletException {
    //     System.out.println("AuthFilterSyncope::doFilter" + (new java.util.Date().toString()));

        

    //     // getClient("bwt-test1", "bwt-test1");
    //     // getClient("bwt-test2", "bwt-test1");
    //     // getClient("admin", "password");
    //     // getClient("bwt-test3@boardwalktech.com", "bwt-test3");

    //     chain.doFilter(request, response);
    // }

    private Cookie getAuthCookie(HttpServletRequest req) {
        Cookie[] cs = req.getCookies();
        Cookie found = null;
        int cookieCtr = 0;
        for (; cookieCtr < cs.length; cookieCtr++) {
            Cookie c = cs[cookieCtr];
            if (IdmServletConstants.hdrToken.equals(c.getName())) {
                found = c;
                break;
            }
        }
        return found;
    }
    // private void getClient(String usr, String pwd) {
    //     System.out.println("Attempting: " + usr);
    //     try {
    //         SyncopeClient s = new SyncopeClientFactoryBean().
    //             setContentType(ContentType.JSON).
    //             setAddress("http://localhost:8888/syncope/rest").
    //             create(usr, pwd);
    //         System.out.println(s.getJWT());
    //         UserService us = s.getService(UserService.class);
    //         PagedResult<UserTO> users = us.search(new AnyQuery());
    //         for (UserTO oneUser : users.getResult()) {
    //             System.out.println(oneUser.getUsername() + " : " + oneUser.getRealm());
    //         }
    //         System.out.println("Success: " + usr);
    //     } catch (Exception exc) {
    //         System.out.println(exc.getMessage());
    //         exc.printStackTrace();
    //     }
    // }
}
