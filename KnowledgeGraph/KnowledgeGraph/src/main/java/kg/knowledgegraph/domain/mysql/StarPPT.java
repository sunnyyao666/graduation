package kg.knowledgegraph.domain.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author YHT
 **/
@Entity
public class StarPPT extends BaseEntity {
    private Long pptID;

    @ManyToOne
    @JsonIgnore
    private User user;

    public StarPPT() {
    }

    public StarPPT(Long pptID, User user) {
        this.pptID = pptID;
        this.user = user;
    }

    public String getPptID() {
        return String.valueOf(pptID);
    }

    public void setPptID(Long pptID) {
        this.pptID = pptID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
