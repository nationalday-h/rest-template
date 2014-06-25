package com.rest.hgq.api.person;

import com.rest.hgq.entity.Users;
import com.rest.hgq.facade.person.UsersFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 上午10:34
 * To change this template use File | Settings | File Templates.
 */
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
public class UsersResource {
    private static final Logger logger = LoggerFactory.getLogger(UsersResource.class);

    private UsersFacade usersFacade;

    @Inject
    public UsersResource(UsersFacade usersFacade) {
        this.usersFacade = usersFacade;
    }

    @GET
    @Path("{userid}")
    public Users getUser(@PathParam("userid") Integer userid) {
        return usersFacade.getUsers(userid);
    }

    @POST
    public Response addUser(Users users) {
        Users users1 = usersFacade.createUser(users);
        return Response.status(Response.Status.CREATED).entity(users1).build();
    }

    @PUT
    public Response updateUser(Users users){
        Users users1=usersFacade.updateUser(users);
        return Response.status(Response.Status.ACCEPTED).entity(users1).build();

    }
}
