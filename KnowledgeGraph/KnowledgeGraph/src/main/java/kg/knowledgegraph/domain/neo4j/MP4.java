package kg.knowledgegraph.domain.neo4j;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author YHT
 **/
@NodeEntity
public class MP4 {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String url;

    @Relationship(type = "MP4")
    private Set<Knowledge> knowledgeSet = new HashSet<>();

    public MP4() {
    }

    public MP4(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getId() {
        return String.valueOf(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Knowledge> getKnowledgeSet() {
        TreeSet<Knowledge> result = new TreeSet<>((o1, o2) -> {
            if (!Objects.equals(o1.getPriority(), o2.getPriority())) {
                return o1.getPriority().compareTo(o2.getPriority());
            }
            return o1.getName().compareTo(o2.getName());
        });
        result.addAll(this.knowledgeSet);
        return result;
    }

    public void setKnowledgeSet(Set<Knowledge> knowledgeSet) {
        this.knowledgeSet = knowledgeSet;
    }
}
