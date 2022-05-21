package kg.knowledgegraph.controller;

import kg.knowledgegraph.repository.neo4j.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YHT
 **/
@RestController
public class LessonController {
    private final LessonRepository lessonRepository;

    @Autowired
    public LessonController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/lesson/get")
    public ResponseEntity<?> getLesson(@RequestParam String name) {
        return ResponseEntity.ok(lessonRepository.findByName(name));
    }
}
