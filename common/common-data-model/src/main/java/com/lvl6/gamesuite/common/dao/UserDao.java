package com.lvl6.gamesuite.common.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.gamesuite.common.po.User;

public interface UserDao extends JpaRepository<User, String> {

    public List<User> findByNameStrangersSee(String nameStrangersSee);

    public List<User> findByEmail(String email);

    public List<User> findByFacebookId(String facebookId);

    public List<User> findByFacebookIdIn(Collection<String> facebookId);

    public List<User> findByFacebookIdOrEmail(String facebookId, String email);

    public List<User> findByEmailOrNameStrangersSeeAndPasswordIsNotNull(String email, String nameStrangersSee);

    public User findById(String id);

    //test this (spring doesn't play well with Map<>
    //public Map<String, User> findByIdIn(Collection<String> idList);
    public List<User> findByIdIn(Collection<String> idList);

}
