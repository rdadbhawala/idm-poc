import java.io.IOException;
import java.util.List;

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
            List<String> roles = new IdmActionsSyncope().verify(found.getValue());
            for (String oneRole : roles) {
                System.out.println("Role: " + oneRole);
            }
            System.out.println("Path Info     : " + req.getPathInfo());
            System.out.println("Context Path  : " + req.getContextPath());
            System.out.println("Servlet Path  : " + req.getServletPath());
            System.out.println("Path Transated: " + req.getPathTranslated());
            super.doFilter(req, res, chain);
        } else {
            res.getWriter().write("IDM FAILURE");
        }
    }

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
}
