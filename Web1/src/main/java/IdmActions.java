public interface IdmActions
{
    String login(String usr, String pwd);
    void logout(String jwt);
    void verify(String jwt);
}
