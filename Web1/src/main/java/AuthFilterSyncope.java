import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.syncope.client.lib.BasicAuthenticationHandler;
import org.apache.syncope.client.lib.SyncopeClient;
import org.apache.syncope.client.lib.SyncopeClientFactoryBean;
import org.apache.syncope.client.lib.SyncopeClientFactoryBean.ContentType;
import org.apache.syncope.common.rest.api.service.UserService;

public class AuthFilterSyncope implements IAuthFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("AuthFilterSyncope::doFilter" + (new java.util.Date().toString()));
        getClient("bwt-test1", "bwt-test1");
        getClient("bwt-test2", "bwt-test1");
        getClient("admin", "password");
        getClient("bwt-test3@boardwalktech.com", "bwt-test3");

        chain.doFilter(request, response);
    }

    private void getClient(String usr, String pwd) {
        System.out.println("Attempting: " + usr);
        try {
            SyncopeClient s = new SyncopeClientFactoryBean().
                setContentType(ContentType.JSON).
                setAddress("http://localhost:8888/syncope/rest").
                create(usr, pwd);
            UserService us = s.getService(UserService.class);
            System.out.println("Success: " + usr);
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        }
    }
}
