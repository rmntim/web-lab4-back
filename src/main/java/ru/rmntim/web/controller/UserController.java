package ru.rmntim.web.controller;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.UserPrincipal;
import ru.rmntim.web.dto.ErrorDTO;
import ru.rmntim.web.dto.UpdatePasswordDTO;
import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.service.UserService;

@Path("/users")
@Slf4j
public class UserController {
    @Inject
    private UserService userService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo() {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var user = userService.getUserInfo(userPrincipal.getUserId());
            return Response.ok(user).build();
        } catch (Exception e) {
            log.error("Error retrieving user info: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfoById(@PathParam("userId") long userId) {
        try {
            var user = userService.getUserInfoById(userId);
            return Response.ok(user).build();
        } catch (Exception e) {
            log.error("Error retrieving user info by id: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @PATCH
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserInfo(@Valid UserInfoDTO userInfo) {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var user = userService.updateUserInfo(userPrincipal.getUserId(), userInfo);
            return Response.ok(user).build();
        } catch (Exception e) {
            log.error("Error updating user info: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @PATCH
    @Path("/password")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassword(@Valid UpdatePasswordDTO passwordDTO) {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            userService.updatePassword(userPrincipal.getUserId(), passwordDTO.getCurrentPassword(), passwordDTO.getNewPassword());
            return Response.ok().build();
        } catch (Exception e) {
            log.error("Error updating password: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Server error")).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser() {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            userService.deleteUser(userPrincipal.getUserId());
            var cookie = new NewCookie.Builder("token").maxAge(0).path("/").httpOnly(true).value("").build();
            return Response.ok().cookie(cookie).build();
        } catch (Exception e) {
            log.error("Error deleting user: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Server error")).build();
        }
    }

    @POST
    @Path("/avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    // NOTE: idea cries for some reason that EntityPart is forbidden to use, also GIFs don't work :)
    public Response uploadAvatar(@SuppressWarnings("RestParamTypeInspection")
                                 @FormParam("file")
                                 EntityPart file) {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var mediaType = file.getMediaType();
            if (!mediaType.isCompatible(MediaType.valueOf("image/*"))) {
                return Response
                        .status(Response.Status.UNSUPPORTED_MEDIA_TYPE)
                        .entity(ErrorDTO.of("Only images are supported"))
                        .build();
            }

            var inputStream = file.getContent();
            var userInfo = userService.uploadAvatar(userPrincipal.getUserId(), inputStream, mediaType);

            return Response.accepted(userInfo).build();
        } catch (Exception e) {
            log.error("Error uploading avatar: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Server error")).build();
        }
    }
}
