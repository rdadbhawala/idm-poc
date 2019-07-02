import org.apache.syncope.client.lib.SyncopeClientFactoryBean;

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
    public void verify(String jwt) {
        new SyncopeClientFactoryBean().
            setAddress(address).
            create(jwt);
    }
}
