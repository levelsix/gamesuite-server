package com.lvl6.gamesuite.common.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.gamesuite.common.po.AuthorizedDevice;

public interface AuthorizedDeviceDao extends JpaRepository<AuthorizedDevice, String> {

    public List<AuthorizedDevice> findByUserIdAndUdid(String userId, String udid);
    
    public List<AuthorizedDevice> findByUserIdAndIdNotIn(String userId, Collection<String> id);
}
