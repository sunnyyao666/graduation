package kg.knowledgegraph.controller;

import kg.knowledgegraph.domain.neo4j.Chapter;
import kg.knowledgegraph.domain.neo4j.Knowledge;
import kg.knowledgegraph.domain.neo4j.MP4;
import kg.knowledgegraph.domain.neo4j.PPT;
import kg.knowledgegraph.repository.neo4j.ChapterRepository;
import kg.knowledgegraph.repository.neo4j.KnowledgeRepository;
import kg.knowledgegraph.repository.neo4j.MP4Repository;
import kg.knowledgegraph.repository.neo4j.PPTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author YHT
 **/
@RestController
public class KnowledgeController {
    private final ChapterRepository chapterRepository;
    private final KnowledgeRepository knowledgeRepository;
    private final PPTRepository pptRepository;
    private final MP4Repository mp4Repository;

    @Autowired
    public KnowledgeController(ChapterRepository chapterRepository, KnowledgeRepository knowledgeRepository, PPTRepository pptRepository, MP4Repository mp4Repository) {
        this.chapterRepository = chapterRepository;
        this.knowledgeRepository = knowledgeRepository;
        this.pptRepository = pptRepository;
        this.mp4Repository = mp4Repository;
    }

    @GetMapping("/knowledge/get")
    public ResponseEntity<?> getKnowledge(@RequestParam String id) {
        Knowledge knowledge = knowledgeRepository.findById(Long.valueOf(id)).orElse(new Knowledge());

        Chapter parent = chapterRepository.findById(Long.valueOf(knowledge.getChapter().getId())).orElse(new Chapter());

        List<PPT> pptList = new LinkedList<>();
        for (PPT ppt : knowledge.getPptSet()) {
            pptList.add(pptRepository.findById(Long.valueOf(ppt.getId())).orElse(new PPT()));
        }

        List<MP4> mp4List = new LinkedList<>();
        for (MP4 mp4 : knowledge.getMp4Set()) {
            mp4List.add(mp4Repository.findById(Long.valueOf(mp4.getId())).orElse(new MP4()));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", knowledge.getId());
        result.put("name", knowledge.getName());
        result.put("info", knowledge.getInfo());
        result.put("parent", parent);
        result.put("parentID", parent.getId());
        result.put("codes", knowledge.getCodes());
        result.put("pre", knowledge.getPredecessors());
        result.put("suc", knowledge.getSuccessors());
        result.put("type", "Knowledge");
        result.put("pptSet", pptList);
        result.put("mp4Set", mp4List);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/knowledge/getPre")
    public ResponseEntity<?> getAllPredecessors(@RequestParam String id) {
        Knowledge knowledge = knowledgeRepository.findById(Long.valueOf(id)).orElse(new Knowledge());
        return ResponseEntity.ok(knowledge.getPredecessors());
    }

    @GetMapping("/knowledge/getSuc")
    public ResponseEntity<?> getAllSuccessors(@RequestParam String id) {
        Knowledge knowledge = knowledgeRepository.findById(Long.valueOf(id)).orElse(new Knowledge());
        return ResponseEntity.ok(knowledge.getSuccessors());
    }

    @GetMapping("/knowledge/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(knowledgeRepository.findAll());
    }

}
