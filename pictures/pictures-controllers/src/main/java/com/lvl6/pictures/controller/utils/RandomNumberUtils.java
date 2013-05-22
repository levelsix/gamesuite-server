package com.lvl6.pictures.controller.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;


public class RandomNumberUtils {

  @Autowired
  protected Random rand;

  public Collection<Integer> generateNRandomIntsBelowInt(
      int upperBound, int amount) {
    Set<Integer> randomInts = new HashSet<Integer>();
    
    //loop until we the number of ints = amount
    while (amount > randomInts.size()) {
      int randInt = rand.nextInt(upperBound);
      randomInts.add(randInt);
    }
    return randomInts;
  }
  
  
  public Random getRand() {
    return rand;
  }

  public void setRand(Random rand) {
    this.rand = rand;
  }
  
}