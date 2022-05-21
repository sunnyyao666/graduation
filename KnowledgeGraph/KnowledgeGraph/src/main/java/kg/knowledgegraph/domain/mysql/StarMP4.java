package kg.knowledgegraph.domain.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author YHT
 **/
@Entity
public class StarMP4 extends BaseEntity {
    private Long mp4ID;

    @ManyToOne
    @JsonIgnore
    private User user;

    public StarMP4() {
    }

    public StarMP4(Long mp4ID, User user) {
        this.mp4ID = mp4ID;
        this.user = user;
    }

    public String getMp4ID() {
        return String.valueOf(mp4ID);
    }

    public void setMp4ID(Long mp4ID) {
        this.mp4ID = mp4ID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
