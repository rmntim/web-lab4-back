package ru.rmntim.web.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.UserPrincipal;
import ru.rmntim.web.dto.ErrorDTO;
import ru.rmntim.web.dto.PointDTO;
import ru.rmntim.web.exceptions.PointNotFoundException;
import ru.rmntim.web.service.PointService;

@Path("/points")
@Slf4j
public class PointController {
    @Inject
    private PointService pointService;

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPoints() {
        try {
            var points = pointService.getPoints();
            return Response.ok(points).build();
        } catch (Exception e) {
            log.error("Error while getting points {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @GET
    @Path("/self")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPoints() {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var points = pointService.getUserPoints(userPrincipal.getUserId());
            return Response.ok(points).build();
        } catch (Exception e) {
            log.error("Error while retrieving points for user: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Internal server error.")).build();
        }
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserPoint(PointDTO pointDTO) {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var createdPoint = pointService.addUserPoint(userPrincipal.getUserId(), pointDTO);
            return Response.ok(createdPoint).build();
        } catch (Exception e) {
            log.error("Error adding point for user {}: {}", userPrincipal.getUserId(), e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of(e.getMessage())).build();
        }
    }

    @DELETE
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUserPoints() {
        var userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            var userId = userPrincipal.getUserId();
            pointService.deleteUserPoints(userId);
            return Response.ok(ErrorDTO.of("All points deleted successfully.")).build();
        } catch (Exception e) {
            log.error("Error deleting all points: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Internal server error")).build();
        }
    }

    @PATCH
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePoint(PointDTO pointDTO) {
        UserPrincipal userPrincipal = (UserPrincipal) securityContext.getUserPrincipal();
        try {
            pointService.deleteSinglePoint(userPrincipal.getUserId(), pointDTO);
            return Response.ok().entity(ErrorDTO.of("Point deleted successfully.")).build();
        } catch (PointNotFoundException e) {
            log.error("Point not found: {}", e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(ErrorDTO.of("Point not found")).build();
        } catch (Exception e) {
            log.error("Error deleting single point: {}", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ErrorDTO.of("Internal server error")).build();
        }
    }
}
