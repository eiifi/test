package com.acme;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/api/users")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UserPrivateRepository {
    @Inject
    EntityManager entityManager;

    @GET
    @RolesAllowed("user")
    @Path("like/{id}")
    @Transactional
    public Response.ResponseBuilder like(@Context SecurityContext securityContext, @PathParam Integer id) {
        List<User> uporabniki = entityManager.createQuery("SELECT u FROM User u where username = :username")
                .setParameter("username",securityContext.getUserPrincipal().getName())
                .getResultList();
       // User entity = entityManager.find(User.class, securityContext.getUserPrincipal().getName());
        if (uporabniki == null) {
            throw new WebApplicationException("User with username of " + " does not exist.", 404);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Like like = new Like(date, uporabniki.get(0).getId(),id );
        entityManager.persist(like);
        return Response.ok().status(201);
    }
    @GET
    @RolesAllowed("user")
    @Path("/me")
    @Transactional
    public User change(@Context SecurityContext securityContext) {
        List<User> users = entityManager.createQuery("SELECT u FROM User u where username = :username")
                .setParameter("username",securityContext.getUserPrincipal().getName())
                .getResultList();
        // User entity = entityManager.find(User.class, securityContext.getUserPrincipal().getName());
        if (users == null) {
            throw new WebApplicationException("User with username of " + " does not exist.", 404);
        }
        User entity = entityManager.find(User.class, users.get(0).getId());
        return entity;
    }

    @GET
    @RolesAllowed("user")
    @Path("/newPass/{id}")
    @Transactional
    public Response changepass(@Context SecurityContext securityContext, @PathParam String id) {
        List<User> users = entityManager.createNamedQuery("Users.findOneByName", User.class)
                .setParameter("username",securityContext.getUserPrincipal().getName())
                .getResultList();
        // User entity = entityManager.find(User.class, securityContext.getUserPrincipal().getName());
        if (users == null) {
            throw new WebApplicationException("User with username of " + " does not exist.", 404);
        }
        User entity = entityManager.find(User.class, users.get(0).getId());
        entity.setPassword(id);
        entityManager.merge(entity);
        return Response.status(202).build();
    }

    Logger logger = Logger.getLogger(UserPrivateRepository.class.getName());
    @GET
    @RolesAllowed("user")
    @Path("unlike/{id}")
    @Transactional
    public Response.ResponseBuilder unlike(@Context SecurityContext securityContext, @PathParam Integer id) {
        logger.log(Level.WARNING, "warning id  "+ id.toString());

            List<Like> likes = entityManager.createQuery("SELECT u FROM Like u where Id_geter = :username")
                    .setParameter("username",id.toString())
                    .getResultList();
            logger.log(Level.WARNING, "stevilo  "+ likes.size());
            if(likes.size() == 0){
                throw new WebApplicationException("User with id of " + id + " dont have likes.", 404);
            }
            // User entity = entityManager.find(User.class, securityContext.getUserPrincipal().getName());

            List<User> uporabniki = entityManager.createNamedQuery("Users.findOneByName", User.class)
                    .setParameter("username",securityContext.getUserPrincipal().getName())
                    .getResultList();
            Integer a = 0;
            for (int i = 0; i < likes.size(); i++) {
                if(likes.get(i).getId_giver() == uporabniki.get(0).getId()){
                    a = i;
                }
            }
            logger.log(Level.WARNING, "banana "+ uporabniki.get(a));
            entityManager.remove(likes.get(a));
            return Response.ok().status(200);



    }

}

