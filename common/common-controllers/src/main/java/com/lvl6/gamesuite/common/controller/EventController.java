package com.lvl6.gamesuite.common.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.gamesuite.common.events.GameEvent;
import com.lvl6.gamesuite.common.events.RequestEvent;



public abstract class EventController {



  private static Logger log = LoggerFactory.getLogger(new Object() {}.getClass().getEnclosingClass());

  // we have the controllers call server.writeEvent manually already
  // /**
  // * utility method for sending events
  // */
  // protected void sendEvent(ResponseEvent e, ConnectedPlayer p) {
  // server.writeEvent(e);
  // }

  /**
   * GameController subclasses should implement initController in order to do
   * any initialization they require.
   */
  protected void initController() {
  }

  /**
   * factory method for fetching GameEvent objects
   */
  public abstract RequestEvent createRequestEvent();

  /**
   * subclasses must implement to do their processing
   * 
   * @throws Exception
   */

  protected void processEvent(GameEvent event) throws Exception {
    final RequestEvent reqEvent = (RequestEvent) event;
/*    MiscMethods
        .setMDCProperties(
            null,
            reqEvent.getPlayerId(),
            MiscMethods.getIPOfPlayer(server,
                reqEvent.getPlayerId(), null));*/
    log.info("Received event: {}", event.getClass().getSimpleName());

    final long startTime = System.nanoTime();
    final long endTime;
    try {
      Exception e = doInTransaction(reqEvent);
      if (e != null) {
        throw e;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      endTime = System.nanoTime();
    }
    double numSeconds = (endTime - startTime) / 1000000;

    log.info("Finished processing event: {}, took ~{}ms", event.getClass().getSimpleName(), numSeconds);
/*
    if (numSeconds / 1000 > Globals.NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING) {
      log.warn("Event {} took over {} seconds", event.getClass().getSimpleName(),  Globals.NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING);
    }

    MiscMethods.purgeMDCProperties();*/
  }

  protected Exception doInTransaction(final RequestEvent reqEvent) {
//    return transactionTemplate
//        .execute(new TransactionCallback<Exception>() {
//
//          @Override
//          public Exception doInTransaction(TransactionStatus arg0) {
            try {
              processRequestEvent(reqEvent);
              return null;
            } catch (Exception e) {
              return e;
            }
//          }
//        });
  }

  /**
   * subclasses must implement to provide their Event type
   */
  public abstract int getEventType();


  protected abstract void processRequestEvent(RequestEvent event)
      throws Exception;

  protected int numAllocatedThreads = 0;

  public int getNumAllocatedThreads() {
    return numAllocatedThreads;
  }

}
