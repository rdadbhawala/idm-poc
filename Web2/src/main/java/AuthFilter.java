import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter
        extends HttpFilter {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        Cookie found = getAuthCookie(req.getCookies());
        if (found != null) {
            List<String> roles = new IdmActionsSyncope().verify(found.getValue());

            // this map can be based on a config file containing mappings
            String expectedRole = null;
            switch (req.getServletPath()) {
                case "/pages/user.jsp":
                    expectedRole = "BWT_User";
                    break;
                case "/pages/support.jsp":
                    expectedRole = "BWT_Support";
                    break;
            }
            if (expectedRole != null && roles.contains(expectedRole)) {
                super.doFilter(req, res, chain);
            } else {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("Unauthorized");
            }
        } else {
            res.setHeader(IAuthConstants.hdrContextPath, req.getContextPath());
            res.sendRedirect(IAuthConstants.urlUsersIdm + req.getContextPath() + req.getServletPath());
        }
    }

    private Cookie getAuthCookie(Cookie[] cs) {
        Cookie found = null;
        if (cs != null && cs.length > 0) {
            for (int cookieCtr = 0; cookieCtr < cs.length; cookieCtr++) {
                Cookie c = cs[cookieCtr];
                if (IAuthConstants.hdrToken.equals(c.getName())) {
                    found = c;
                    break;
                }
            }
        }
        return found;
    }
}
