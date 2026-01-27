package io.github.Eduardo_Port.userhubapi.controller;

import io.github.Eduardo_Port.userhubapi.annotation.ApiStandardErrors;
import io.github.Eduardo_Port.userhubapi.dtos.ApiErrorMessage;
import io.github.Eduardo_Port.userhubapi.dtos.UserRequest;
import io.github.Eduardo_Port.userhubapi.dtos.UserResponse;
import io.github.Eduardo_Port.userhubapi.model.User;
import io.github.Eduardo_Port.userhubapi.service.UserHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.UUID;

@RestController
@Tag(name = "User", description = "Controller para salvar, editar e consumir dados de usuário.")
public class UserHubController {
    @Autowired
    private UserHubService service;
    @PostMapping
    @Operation(summary = "Salva dados de um novo usuário", description = "Método para registrar novos usuários no banco de dados.")
    @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso")
    @ApiStandardErrors
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userData) {
        User user = service.createUser(userData);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getIdUser()).toUri();
        UserResponse response = new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Desativa o usuário", description = "Método para desabilitar algum usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário desabilitado com sucesso")
    @ApiStandardErrors
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reativa um usuário", description = "Método para reabilitar algum usuário desativado.")
    @ApiResponse(responseCode = "200", description = "Usuário reabilitado com sucesso")
    @ApiStandardErrors
    @PutMapping("/reactivate/{email}")
    public ResponseEntity<Void> reactivateUser(@PathVariable String email) {
        service.reactivateUser(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualiza os dados do usuário", description = "Método para atualizar o nome ou a senha do usuário.")
    @ApiResponse(responseCode = "201", description = "Dados atualizados com sucesso")
    @ApiStandardErrors
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UserRequest dto, @PathVariable UUID id) {
        User user = service.update(id, dto);
        URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        UserResponse response = new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "Exibe dados do usuário", description = "Método para ler dados do usuário.")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso")
    @ApiStandardErrors
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable UUID id) {

        return service.findUserById(id)
                .map(user -> {
                    UserResponse response = new UserResponse(user);
                    return ResponseEntity.ok(response);
                }).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Operation(summary = "Exibe dados de usuários com filtros opcionais", description = "Método para ler dados de vários usuários de forma paginada.")
    @ApiResponse(responseCode = "200", description = "Busca no banco realizada com sucesso, caso não haja usuários o conteúdo estará vazio")
    @ApiResponse(responseCode = "500", description = "Erro no servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorMessage.class), examples =  {@ExampleObject(value = "No body returned for response")}))
    @GetMapping("/list")
    public ResponseEntity<Page<UserResponse>> listUsers(
            @RequestParam(
                required = false,
                defaultValue = "0") int page,
            @RequestParam(
                required = false,
                defaultValue = "3") int size,
            @RequestParam(
                    required = false
            ) String name,
            @RequestParam(required = false) String email) {
        Page<User> users = service.getUsers(page, size, name, email);
        Page<UserResponse> res = users.map(user -> {
            URI loc = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getIdUser()).toUri();
            return new UserResponse(user.getIdUser(), user.getName(), user.getEmail(), user.getEmailVerified(), user.getCreatedAt(), user.getUpdatedAt(), loc);
        });
        return ResponseEntity.ok(res);
    }
}
