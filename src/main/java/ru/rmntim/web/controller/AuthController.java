package ru.rmntim.web.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.UserPrincipal;
import ru.rmntim.web.dto.ErrorDTO;
import ru.rmntim.web.dto.SimpleUserDTO;
import ru.rmntim.web.dto.UserDTO;
import ru.rmntim.web.exceptions.AuthenticationException;
import ru.rmntim.web.exceptions.InvalidEmailException;
import ru.rmntim.web.exceptions.ServerException;
import ru.rmntim.web.exceptions.UserExistsException;
import ru.rmntim.web.service.AuthService;

import java.time.Duration;

@Path("/auth")
@Slf4j
public class AuthController {
    private static final NewCookie.Builder COOKIE = new NewCookie.Builder("token")
            .maxAge((int) Duration.ofMinutes(30).toSeconds())
            .path("/")
            .httpOnly(true);

    @Inject
    private AuthService authService;

    @Context
    private SecurityContext securityContext;

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@Valid UserDTO userDto) {
        try {
            var tokenAndUserInfo = authService.registerUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
            var cookie = COOKIE.value(tokenAndUserInfo.getLeft()).build();
            return Response.ok(tokenAndUserInfo.getRight()).cookie(cookie).build();
        } catch (UserExistsException | InvalidEmailException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(ErrorDTO.of(e.getMessage())).build();
        } catch (ServerException e) {
            log.error(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid SimpleUserDTO userDto) {
        try {
            var tokenAndUserInfo = authService.authenticateUser(userDto.getEmail(), userDto.getPassword());
            var cookie = COOKIE.value(tokenAndUserInfo.getLeft()).build();
            return Response.ok(tokenAndUserInfo.getRight()).cookie(cookie).build();
        } catch (AuthenticationException e) {
            log.error("Login failed for user with email: {}", userDto.getEmail());
            return Response.status(Response.Status.UNAUTHORIZED).entity(ErrorDTO.of(e.getMessage())).build();
        } catch (ServerException e) {
            log.error("Internal server error: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {
        try {
            var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
            authService.endSession(userPrincipal.getUserId());
            var cookie = COOKIE.value("").build();
            return Response.ok().cookie(cookie).build();
        } catch (Exception e) {
            log.error("Error during logout: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Error during logout"))
                    .build();
        }
    }
}