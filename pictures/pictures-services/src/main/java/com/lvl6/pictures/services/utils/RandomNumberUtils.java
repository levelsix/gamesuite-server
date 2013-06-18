package com.lvl6.pictures.services.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class RandomNumberUtils {

  protected Random rand = new Random();

  public Collection<Integer> generateNRandomIntsBelowInt(
      int upperBound, int n) {
    Set<Integer> randomInts = new HashSet<Integer>();
    
    //loop until we have number of ints = amount
    while (n > randomInts.size()) {
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