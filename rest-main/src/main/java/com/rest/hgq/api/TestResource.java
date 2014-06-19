package com.rest.hgq.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 下午2:26
 * To change this template use File | Settings | File Templates.
 */
@Path("test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

    @GET
    public Response testGet(){
        return Response.status(Response.Status.OK).entity("测试get返回").build();
    }
}
