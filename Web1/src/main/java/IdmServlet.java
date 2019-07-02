import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IdmServlet extends HttpServlet implements IdmServletConstants {
    private static final long serialVersionUID = 1L;

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // super.service(req, resp);
        String action = req.getParameter(prmAction);
        try {
            switch (action) {
                case actLogin:
                    this.doLogin(req, resp);
                    break;
                case actLogout:
                    this.doLogout(req, resp);
                    break;
                case actVerify:
                    this.doVerify(req, resp);
                    break;
                default:
                    throw new RuntimeException("Unknown Action");
            }
            resp.getWriter().println("SUCCESS");
        } catch (Exception exc) {
            PrintWriter w = resp.getWriter();
            w.write("<br />");
            w.println(exc.getMessage());
            exc.printStackTrace(w);
        }
    }

    private void doVerify(HttpServletRequest req, HttpServletResponse resp) {
        Cookie found = getAuthCookie(req);
        if (found != null) {
            new IdmActionsSyncope().verify(found.getValue());
        }
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) {
        Cookie found = getAuthCookie(req);
        if (found != null) {
            new IdmActionsSyncope().logout(found.getValue());
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

    private void doLogin(HttpServletRequest req, HttpServletResponse resp) {
        String token = new IdmActionsSyncope().login(req.getParameter(prmUser), req.getParameter(prmPassword));
        Cookie c = new Cookie(IdmServletConstants.hdrToken, token);
        c.setPath(req.getContextPath());
        // can also set the expiry here on the cookie
        resp.addCookie(c);
    }
}
