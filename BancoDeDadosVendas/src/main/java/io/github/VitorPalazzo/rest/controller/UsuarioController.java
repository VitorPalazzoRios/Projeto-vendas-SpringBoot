package io.github.VitorPalazzo.rest.controller;

import io.github.VitorPalazzo.domain.entity.Usuario;
import io.github.VitorPalazzo.exception.SenhaInvalidaException;
import io.github.VitorPalazzo.rest.dto.CredenciaisDTO;
import io.github.VitorPalazzo.rest.dto.TokenDTO;
import io.github.VitorPalazzo.security.jwt.JwtService;
import io.github.VitorPalazzo.service.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioServiceImpl usuarioService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario salvar(@RequestBody @Valid Usuario usuario){
        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/auth")
    public TokenDTO autenticar (@RequestBody CredenciaisDTO credenciaisDTO){
    try {
        Usuario usuario = Usuario.builder().login(credenciaisDTO.getLogin()
        ).senha(credenciaisDTO.getSenha()).build();
        UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
        String token = jwtService.gerarToken(usuario);
        return new TokenDTO(usuario.getLogin(), token);
    }catch (UsernameNotFoundException e){
    throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    } catch (SenhaInvalidaException e ){
        throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
    }
}
