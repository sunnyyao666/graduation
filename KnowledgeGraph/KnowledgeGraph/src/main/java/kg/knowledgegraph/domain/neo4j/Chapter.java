package kg.knowledgegraph.domain.neo4j;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


import java.util.*;

/**
 * @author YHT
 **/
@NodeEntity
public class Chapter {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer priority;

    @JsonIgnore
    @Relationship(type = "CONTAIN", direction = Relationship.INCOMING)
    private Lesson lesson;

    @Relationship(type = "CONTAIN")
    private Set<Knowledge> knowledgeSet = new HashSet<>();

    public Chapter() {
    }

    public Chapter(String name) {
        this.name = name;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getType() {
        return "Chapter";
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
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

