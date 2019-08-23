package br.com.tilmais.tilmhat.controller;

import br.com.tilmais.tilmhat.dto.UserRequestDTO;
import br.com.tilmais.tilmhat.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(UserController.PATH_PARENT)
public class UserController {

    static final String PATH_PARENT = "/users";

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR','MASTER')")
    public ResponseEntity register(@Valid @RequestBody UserRequestDTO dto) {
        final Optional<URI> optionalURI = this.userService.registerUser(dto);
        if (optionalURI.isPresent()) return ResponseEntity.created(optionalURI.get()).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
