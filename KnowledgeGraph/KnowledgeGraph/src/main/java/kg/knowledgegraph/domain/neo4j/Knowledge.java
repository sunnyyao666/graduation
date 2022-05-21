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
public class Knowledge {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String info;

    private Long parentID;

    private Integer priority;

    @JsonIgnore
    @Relationship(type = "CONTAIN", direction = Relationship.INCOMING)
    private Chapter chapter;

    @Relationship(type = "CODE", direction = Relationship.INCOMING)
    private Set<Code> codes = new HashSet<>();

    @Relationship(type = "PRE", direction = Relationship.INCOMING)
    private Set<Knowledge> predecessors = new HashSet<>();

    @JsonIgnore
    @Relationship(type = "PRE")
    private Set<Knowledge> successors = new HashSet<>();

    @JsonIgnore
    @Relationship(type = "PPT", direction = Relationship.INCOMING)
    private Set<PPT> pptSet = new HashSet<>();

    @JsonIgnore
    @Relationship(type = "MP4", direction = Relationship.INCOMING)
    private Set<MP4> mp4Set = new HashSet<>();

    public Knowledge() {
    }

    public Knowledge(String name, String info) {
        this.name = name;
        this.info = info;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getParentID() {
        return String.valueOf(parentID);
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getType() {
        return "Knowledge";
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Set<Code> getCodes() {
        TreeSet<Code> result = new TreeSet<>(Comparator.comparingInt(Code::getPriority));
        result.addAll(this.codes);
        return result;
    }

    public void setCodes(Set<Code> codes) {
        this.codes = codes;
    }

    public Set<Knowledge> getPredecessors() {
        TreeSet<Knowledge> result = new TreeSet<>((o1, o2) -> {
            if (!Objects.equals(o1.getPriority(), o2.getPriority())) {
                return o1.getPriority().compareTo(o2.getPriority());
            }
            return o1.getName().compareTo(o2.getName());
        });
        result.addAll(this.predecessors);
        return result;
    }

    public void setPredecessors(Set<Knowledge> predecessors) {
        this.predecessors = predecessors;
    }

    public Set<Knowledge> getSuccessors() {
        TreeSet<Knowledge> result = new TreeSet<>((o1, o2) -> {
            if (!Objects.equals(o1.getPriority(), o2.getPriority())) {
                return o1.getPriority().compareTo(o2.getPriority());
            }
            return o1.getName().compareTo(o2.getName());
        });
        result.addAll(this.successors);
        return result;
    }

    public void setSuccessors(Set<Knowledge> successors) {
        this.successors = successors;
    }

    public Set<PPT> getPptSet() {
        TreeSet<PPT> result = new TreeSet<>(Comparator.comparing(PPT::getName));
        result.addAll(this.pptSet);
        return result;
    }

    public void setPptSet(Set<PPT> pptSet) {
        this.pptSet = pptSet;
    }

    public Set<MP4> getMp4Set() {
        TreeSet<MP4> result = new TreeSet<>(Comparator.comparing(MP4::getName));
        result.addAll(this.mp4Set);
        return result;
    }

    public void setMp4Set(Set<MP4> mp4Set) {
        this.mp4Set = mp4Set;
    }
}
