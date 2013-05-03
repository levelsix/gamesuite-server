package com.lvl6.pictures.po;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class GameHistory extends BasePersistentObject {
  
  @NotNull
  @Index(name = "player_one_id_index")
  protected String playerOneId;

  @NotNull
  @Index(name = "player_two_id_index")
  protected String playerTwoId;
  
  @Temporal(TemporalType.TIMESTAMP)
  protected Date startTime = new Date();
  
  //if not null then game is completed
  @Temporal(TemporalType.TIMESTAMP)
  protected Date endTime;
  
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  protected Set<RoundHistory> roundHistory;
  
}