package com.lvl6.pictures.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.Currency;

public interface CurrencyDao extends JpaRepository<Currency, String> {
  
  public Currency findByUserId(String userId); 
  
}
