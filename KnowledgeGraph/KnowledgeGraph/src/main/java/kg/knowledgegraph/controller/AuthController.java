package kg.knowledgegraph.controller;

import kg.knowledgegraph.exception.DuplicateUsernameException;
import kg.knowledgegraph.exception.LogInFailException;
import kg.knowledgegraph.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import kg.knowledgegraph.controller.request.UserAuthRequest;
import kg.knowledgegraph.domain.mysql.User;
import kg.knowledgegraph.repository.mysql.UserRepository;

/**
 * @author YHT
 **/
@RestController
public class AuthController {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.encoder = encoder;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok(userRepository.findByUsername("admin"));
    }

    @PostMapping("/user/logIn")
    public ResponseEntity<?> userLogIn(@RequestBody UserAuthRequest request) throws LogInFailException {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            throw new LogInFailException();
        }

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new LogInFailException();
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> userRegister(@RequestBody UserAuthRequest request) throws DuplicateUsernameException {
        String username = request.getUsername();
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new DuplicateUsernameException(username);
        }

        user = new User(username, encoder.encode(request.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/get")
    public ResponseEntity<?> getUser() throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/user/change")
    public ResponseEntity<?> changePassword(@RequestBody UserAuthRequest request) throws BadCredentialsException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) throw new BadCredentialsException("Not authorized.");
        User user = userRepository.findByUsername(userDetails.getUsername());
        if (user == null) {
            throw new BadCredentialsException("Not authorized.");
        }

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Not authorized.");
        }

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }
}
