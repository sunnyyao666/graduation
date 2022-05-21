package kg.knowledgegraph.domain.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


/**
 * @author YHT
 */
@Entity
public class Authority extends BaseEntity implements GrantedAuthority {

    private String authority;

    @ManyToOne
    @JsonIgnore
    private User user;


    public Authority() {
    }

    public Authority(String authority, User user) {
        this.authority = authority;
        this.user = user;
    }


    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
