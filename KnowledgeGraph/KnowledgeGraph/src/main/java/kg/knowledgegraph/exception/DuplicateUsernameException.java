package kg.knowledgegraph.exception;

/**
 * @author YHT
 **/
public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String username) {
        super("用户名" + username + "已被注册");
    }
}
