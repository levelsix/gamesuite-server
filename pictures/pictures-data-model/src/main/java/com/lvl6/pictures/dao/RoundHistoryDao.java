package com.lvl6.pictures.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.RoundHistory;

public interface RoundHistoryDao extends JpaRepository<RoundHistory, String> {

}
