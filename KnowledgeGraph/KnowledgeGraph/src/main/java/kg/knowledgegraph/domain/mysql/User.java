package kg.knowledgegraph.domain.mysql;


import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.*;

/**
 * @author YHT
 **/
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true)
    private String username;

    private String password;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<StarPPT> starPPTSet = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER, mappedBy = "user")
    private Set<StarMP4> starMP4Set = new HashSet<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Set<Authority> getAuthorities() {
        return null;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<StarPPT> getStarPPTSet() {
        return starPPTSet;
    }

    public void setStarPPTSet(Set<StarPPT> starPPTSet) {
        this.starPPTSet = starPPTSet;
    }

    public Set<StarMP4> getStarMP4Set() {
        return starMP4Set;
    }

    public void setStarMP4Set(Set<StarMP4> starMP4Set) {
        this.starMP4Set = starMP4Set;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
