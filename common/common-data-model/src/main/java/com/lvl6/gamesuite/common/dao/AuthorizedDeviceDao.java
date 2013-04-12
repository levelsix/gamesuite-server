package com.lvl6.gamesuite.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.gamesuite.common.po.AuthorizedDevice;

public interface AuthorizedDeviceDao extends JpaRepository<AuthorizedDevice, String> {

}
