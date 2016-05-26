package net.christopherliu.cacooapi;

import net.christopherliu.cacooapi.types.Diagram;
import net.christopherliu.system.AsyncTaskResult;

/**
 * Created by Christopher Liu on 5/23/2016.
 */
public interface DiagramsListener {
    /**
     * Responds to new incoming AccountInfo
     * @param diagrams
     */
    void handle(AsyncTaskResult<Diagram[]> diagrams);
}
