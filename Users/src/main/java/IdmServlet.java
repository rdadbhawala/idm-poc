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
            if (action != null) {
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
                }
            } else {
                // if token is present, set it else request login
                doRedirect(req, resp);
            }
            resp.getWriter().println("SUCCESS");
        } catch (RuntimeException exc) {
            PrintWriter w = resp.getWriter();
            w.write("<br />");
            w.println(exc.getMessage());
            exc.printStackTrace(w);
        }
    }

    private void doRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie found = getAuthCookie(req.getCookies());
        System.out.println("Context Path: " + req.getContextPath());
        System.out.println("Servlet Path: " + req.getServletPath());
        System.out.println("Request URI : " + req.getRequestURI());
        if (found != null) {
            String targetPath = req.getRequestURI().substring(req.getContextPath().length() + req.getServletPath().length());
            Cookie c = new Cookie(IdmServletConstants.hdrToken, found.getValue());
            c.setDomain("localhost");
            c.setPath("/" + targetPath.split("/")[1]);
            resp.addCookie(c);
            resp.sendRedirect("http://localhost:8888" + targetPath);
        } else {
            PrintWriter w = resp.getWriter();
            w.println("Please Login");
        }
    }

    private void doVerify(HttpServletRequest req, HttpServletResponse resp) {
        Cookie found = getAuthCookie(req.getCookies());
        if (found != null) {
            new IdmActionsSyncope().verify(found.getValue());
        }
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) {
        Cookie found = getAuthCookie(req.getCookies());
        if (found != null) {
            new IdmActionsSyncope().logout(found.getValue());
            Cookie c = new Cookie(IdmServletConstants.hdrToken, "");
            c.setPath(req.getContextPath());
            resp.addCookie(c);
        }
    }

    private Cookie getAuthCookie(Cookie[] cs) {
        Cookie found = null;
        if (cs != null && cs.length > 0) {
            for (int cookieCtr = 0; cookieCtr < cs.length; cookieCtr++) {
                Cookie c = cs[cookieCtr];
                if (IdmServletConstants.hdrToken.equals(c.getName())) {
                    found = c;
                    break;
                }
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
