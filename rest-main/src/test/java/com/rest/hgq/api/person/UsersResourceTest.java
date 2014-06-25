package com.rest.hgq.api.person;

import com.rest.hgq.common.AbstractResourceTest;
import com.rest.hgq.entity.Users;
import com.sun.jersey.api.client.ClientResponse;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public class UsersResourceTest extends AbstractResourceTest {


    @Test
    public void should_return_addUser() throws Exception {
        Users users=new Users();
        users.setUserId(10);
        users.setUsername("test_add");
        users.setPassword("123");
//        ClientResponse response = createSpecificClient().resource(BASE_URI).path("user")
//                .entity(users)
//                .post(ClientResponse.class);
        ClientResponse response = postRequest(users, "user");
        Users ret = response.getEntity(Users.class);
        assertThat(response.getClientResponseStatus(), is(ClientResponse.Status.CREATED));
        assertThat(ret.getUsername(),is(users.getUsername()));
    }

    @Test
    public void should_return_User() throws Exception {
        String userid = "10"; //事先查询表中的user_id
        ClientResponse response = getRequest("user/" + userid);
        Users actual = response.getEntity(Users.class);
        assertThat(actual.getUsername(),is("test_add"));
    }

}
