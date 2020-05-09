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
import java.io.Serializable;
import java.net.URI;

@RestController
@RequestMapping(UserController.PATH_PARENT)
class UserController {

    static final String PATH_PARENT = "/users";

    private final UserService userService;

    UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR','MASTER')")
    public ResponseEntity<Serializable> register(@Valid @RequestBody final UserRequestDTO dto) {
        return this.userService.registerUser(dto)
                .map(this::getCreated)
                .orElseGet(this::getForbidden);
    }

    private ResponseEntity<Serializable> getForbidden() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Serializable> getCreated(final URI uri) {
        return ResponseEntity.created(uri).build();
    }
}
