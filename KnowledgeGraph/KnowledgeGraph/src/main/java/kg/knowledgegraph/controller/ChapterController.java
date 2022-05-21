package kg.knowledgegraph.controller;

import kg.knowledgegraph.domain.neo4j.Chapter;
import kg.knowledgegraph.repository.neo4j.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.TreeSet;


/**
 * @author YHT
 **/
@RestController
public class ChapterController {
    private final ChapterRepository chapterRepository;

    @Autowired
    public ChapterController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @GetMapping("/chapter/getAll")
    public ResponseEntity<?> getAllChapters() {
        TreeSet<Chapter> result = new TreeSet<>(Comparator.comparingInt(Chapter::getPriority));
        for (Chapter c : chapterRepository.findAll()) {
            result.add(c);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/chapter/getKnowledge")
    public ResponseEntity<?> getAllContainKnowledge(@RequestParam String id) {
        Chapter chapter = chapterRepository.findById(Long.valueOf(id)).orElse(new Chapter());
        return ResponseEntity.ok(chapter.getKnowledgeSet());
    }

    @GetMapping("/chapter/get")
    public ResponseEntity<?> getChapter(@RequestParam String id) {
        Chapter chapter = chapterRepository.findById(Long.valueOf(id)).orElse(new Chapter());
        return ResponseEntity.ok(chapter);
    }
}
