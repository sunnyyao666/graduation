package kg.knowledgegraph.domain.neo4j;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author YHT
 **/
@NodeEntity
public class Lesson {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "CONTAIN")
    private Set<Chapter> chapters = new HashSet<>();

    public Lesson() {
    }

    public Lesson(String name) {
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

    public String getType() {
        return "Lesson";
    }

    public Set<Chapter> getChapters() {
        TreeSet<Chapter> result = new TreeSet<>(Comparator.comparingInt(Chapter::getPriority));
        result.addAll(this.chapters);
        return result;
    }

    public void setChapters(Set<Chapter> chapters) {
        this.chapters = chapters;
    }
}
