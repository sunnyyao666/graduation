package kg.knowledgegraph.controller;

import kg.knowledgegraph.domain.mysql.StarMP4;
import kg.knowledgegraph.domain.mysql.StarPPT;
import kg.knowledgegraph.domain.mysql.User;
import kg.knowledgegraph.domain.neo4j.MP4;
import kg.knowledgegraph.domain.neo4j.PPT;
import kg.knowledgegraph.repository.mysql.StarMP4Repository;
import kg.knowledgegraph.repository.mysql.StarPPTRepository;
import kg.knowledgegraph.repository.mysql.UserRepository;
import kg.knowledgegraph.repository.neo4j.MP4Repository;
import kg.knowledgegraph.repository.neo4j.PPTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author YHT
 **/
@RestController
public class UserController {
    private final UserRepository userRepository;
    private final StarPPTRepository starPPTRepository;
    private final StarMP4Repository starMP4Repository;
    private final PPTRepository pptRepository;
    private final MP4Repository mp4Repository;

    @Autowired
    public UserController(UserRepository userRepository, StarPPTRepository starPPTRepository, StarMP4Repository starMP4Repository, PPTRepository pptRepository, MP4Repository mp4Repository) {
        this.userRepository = userRepository;
        this.starPPTRepository = starPPTRepository;
        this.starMP4Repository = starMP4Repository;
        this.pptRepository = pptRepository;
        this.mp4Repository = mp4Repository;
    }

    @GetMapping("/starPPT/add")
    public ResponseEntity<?> addStarPPT(@RequestParam String id) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");

        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        StarPPT starPPT = new StarPPT(Long.valueOf(id), user);
        starPPTRepository.save(starPPT);
        return ResponseEntity.ok(user);
    }

    @Transactional
    @GetMapping("/starPPT/delete")
    public ResponseEntity<?> deleteStarPPT(@RequestParam String id) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");

        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        starPPTRepository.deleteByPptIDAndUser_Id(Long.valueOf(id), user.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/starPPT/getAll")
    public ResponseEntity<?> getAllStarPPTs() throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");

        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        List<StarPPT> starPPTList = starPPTRepository.findAllByUser_IdOrderByCreateTimeDesc(user.getId());
        List<PPT> pptList = new LinkedList<>();
        for (StarPPT s : starPPTList) {
            pptList.add(pptRepository.findById(Long.valueOf(s.getPptID())).orElse(new PPT()));
        }

        return ResponseEntity.ok(pptList);
    }

    @GetMapping("/starMP4/add")
    public ResponseEntity<?> addStarMP4(@RequestParam String id) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");

        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        StarMP4 starMP4 = new StarMP4(Long.valueOf(id), user);
        starMP4Repository.save(starMP4);
        return ResponseEntity.ok(user);
    }

    @Transactional
    @GetMapping("/starMP4/delete")
    public ResponseEntity<?> deleteStarMP4(@RequestParam String id) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");

        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        starMP4Repository.deleteByMp4IDAndUser_Id(Long.valueOf(id), user.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/starMP4/getAll")
    public ResponseEntity<?> getAllStarMP4s() throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");

        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        List<StarMP4> starMP4List = starMP4Repository.findAllByUser_IdOrderByCreateTimeDesc(user.getId());
        List<MP4> mp4List = new LinkedList<>();
        for (StarMP4 s : starMP4List) {
            mp4List.add(mp4Repository.findById(Long.valueOf(s.getMp4ID())).orElse(new MP4()));
        }

        return ResponseEntity.ok(mp4List);
    }
}
