package com.acme;

import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Path("users")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class UserPublicRepository {

    private static final Logger LOGGER = Logger.getLogger(UserPublicRepository.class.getName());

    @Inject
    EntityManager entityManager;

    @GET
    public List<User> get() {
        return entityManager.createNamedQuery("Users.findAll", User.class).getResultList();
    }

    @GET
    @Path("{id}")
    public User getSingle(@PathParam Integer id) {
        User entity = entityManager.find( User.class, id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Path("/singUp")
    @Transactional
    public Response create(User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new WebApplicationException("username was invalidly set on request.", 422);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        user.setRole("user");
        user.setCreated(date);
        entityManager.persist(user);

        return Response.ok(user).status(201).build();
    }
    //json ignroe
   /*  @POST
    @Path("{id}/like")
    @Transactional
  /*  public User likeHandleUP(@PathParam Integer id) {
        User entity = entityManager.find(User.class, id);
        User entity1 = entityManager.find(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        entity.uplike(entity.getLikes());
        entityManager.merge(entity);
     //   entityManager.remove(entity1);
        return entity;
    }

    @POST
    @Path("{id}/dislike")
    @Transactional
    public User likeHandleDown(@PathParam Integer id) {
        User entity = entityManager.find(User.class, id);
        User entity1 = entityManager.find(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        entity.downlike(entity.getLikes());
        entityManager.merge(entity);
        //   entityManager.remove(entity1);
        return entity;
    }
*/


    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam Integer id) {
        User entity = entityManager.getReference(User.class, id);
        if (entity == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        entityManager.remove(entity);
        return Response.status(204).build();
    }


}
