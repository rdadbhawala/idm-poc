import java.util.List;

public interface IdmActions
{
    String login(String usr, String pwd);
    void logout(String jwt);
    List<String> verify(String jwt);
}
