package com.rest.hgq.facade.person;

import com.google.common.base.Strings;
import com.google.inject.persist.Transactional;
import com.rest.hgq.common.exceptions.NotFoundException;
import com.rest.hgq.core.BaseFacade;
import com.rest.hgq.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: huangguoqing
 * Date: 14-6-18
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public class UsersFacade extends BaseFacade {
    private static final Logger logger = LoggerFactory.getLogger(UsersFacade.class);

    @Transactional
    public Users createUser(Users users) {

        Users tmpUsers = persist(users);
        return tmpUsers;
    }

    @Transactional
    public Users updateUser(Users users) {
        return merge(users);
    }

    public Users getUsers(Integer userid) {

        Users tmpUsers  = strictFindByPrimaryKey(Users.class, userid);
        return tmpUsers;
    }

}
