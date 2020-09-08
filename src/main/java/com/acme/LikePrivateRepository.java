package com.acme;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("most")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class LikePrivateRepository {

    @Inject
    EntityManager entityManager;

    @GET
    @Path("liked")
    public String getSingle(){
        List<Like> likes1 = entityManager.createQuery("SELECT u FROM Like u ORDER BY u.id_geter").getResultList();

        int arr[] = new int[100];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }
        for(int i = 0; i< likes1.size(); i++){
            arr[likes1.get(i).getId_geter()]++;
        }
        Integer max = 0;
        Integer index = 0;

        for(int i = 0; i< arr.length; i++){
            if(arr[i] > max){
                max = arr[i];
                index = i;
            }
        }
        return "Id userja = " + index + "; stevilo lajkov = " + arr[index];
    }

}
