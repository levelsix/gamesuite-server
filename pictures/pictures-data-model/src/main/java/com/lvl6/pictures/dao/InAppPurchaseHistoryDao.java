package com.lvl6.pictures.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.InAppPurchaseHistory;

public interface InAppPurchaseHistoryDao extends JpaRepository<InAppPurchaseHistory, String> {

    public List<InAppPurchaseHistory> findByTransactionId(long transactionId); 
}
