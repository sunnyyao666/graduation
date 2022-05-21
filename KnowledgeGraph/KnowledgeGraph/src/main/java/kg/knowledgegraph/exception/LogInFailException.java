package kg.knowledgegraph.exception;

/**
 * @author YHT
 **/
public class LogInFailException extends RuntimeException {
    public LogInFailException() {
        super("用户名或密码错误");
    }
}
