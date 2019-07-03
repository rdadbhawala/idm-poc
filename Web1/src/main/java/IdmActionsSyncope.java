import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.syncope.client.lib.SyncopeClientFactoryBean;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.rest.api.service.UserSelfService;

public class IdmActionsSyncope implements IdmActions
{
    private static final String address = "http://localhost:8888/syncope/rest";
    public IdmActionsSyncope()
    { }

    @Override
    public String login(String usr, String pwd) {
        return new SyncopeClientFactoryBean().
            setAddress(address).
            create(usr, pwd).
            getJWT();
    }

    @Override
    public void logout(String jwt) {
        new SyncopeClientFactoryBean().
            setAddress(address).
            create(jwt).
            logout();
    }

    @Override
    public List<String> verify(String jwt) {
        Response r = new SyncopeClientFactoryBean().
            setAddress(address).
            create(jwt).
            getService(UserSelfService.class).read();
        UserTO u = r.readEntity(UserTO.class);
        return u.getRoles();
    }
}
