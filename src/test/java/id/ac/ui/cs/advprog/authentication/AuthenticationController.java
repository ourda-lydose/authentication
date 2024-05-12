package id.ac.ui.cs.advprog.authentication;

import id.ac.ui.cs.advprog.authentication.dto.LoginUserDto;
import id.ac.ui.cs.advprog.authentication.dto.RegisterUserDto;
import id.ac.ui.cs.advprog.authentication.entities.ERole;
import id.ac.ui.cs.advprog.authentication.entities.User;
import id.ac.ui.cs.advprog.authentication.responses.LoginResponse;
import id.ac.ui.cs.advprog.authentication.services.AuthenticationService;
import id.ac.ui.cs.advprog.authentication.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken((UserDetails) authenticatedUser);
        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/add-role")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public ResponseEntity<User> addRoleToUser(@RequestParam String email, @RequestParam ERole role) {
        User updatedUser = authenticationService.addRoleToUser(email, role);
        return ResponseEntity.ok(updatedUser);
    }
}
