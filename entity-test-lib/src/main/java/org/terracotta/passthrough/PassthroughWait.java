/*
 *
 *  The contents of this file are subject to the Terracotta Public License Version
 *  2.0 (the "License"); You may not use this file except in compliance with the
 *  License. You may obtain a copy of the License at
 *
 *  http://terracotta.org/legal/terracotta-public-license.
 *
 *  Software distributed under the License is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 *  the specific language governing rights and limitations under the License.
 *
 *  The Covered Software is Entity API.
 *
 *  The Initial Developer of the Covered Software is
 *  Terracotta, Inc., a Software AG company
 *
 */
package org.terracotta.passthrough;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.terracotta.entity.InvokeFuture;
import org.terracotta.exception.EntityException;


/**
 * Used by the client-side message processing to handle the synchronous nature of the messaging system.  This expects the
 * client code's thread to block on acks or completion, and be unblocked by the client-send message processing thread
 * processing the corresponding acks and completion messages.
 */
public class PassthroughWait implements InvokeFuture<byte[]> {
  // Save the information used to reset this object on resend.
  private byte[] rawMessageForResend;
  private final boolean shouldWaitForReceived;
  private final boolean shouldWaitForCompleted;
  
  // The active state of the instance after the send.
  private boolean waitingForSent;
  private boolean waitingForReceive;
  private boolean waitingForComplete;
  private boolean didComplete;
  private byte[] response;
  private EntityException error;

  public PassthroughWait(boolean shouldWaitForSent, boolean shouldWaitForReceived, boolean shouldWaitForCompleted) {
    this.shouldWaitForReceived = shouldWaitForReceived;
    this.shouldWaitForCompleted = shouldWaitForCompleted;
    
    this.waitingForSent = shouldWaitForSent;
    this.waitingForReceive = shouldWaitForReceived;
    this.waitingForComplete = shouldWaitForCompleted;
    this.didComplete = false;
    this.response = null;
    this.error = null;
  }
  
  public synchronized void waitForAck() {
    while (this.waitingForSent || this.waitingForReceive || this.waitingForComplete) {
      try {
        wait();
      } catch (InterruptedException e) {
        Assert.unexpected(e);
      }
    }
  }

  @Override
  public void interrupt() {
    throw new IllegalStateException("Not supported");
  }

  @Override
  public synchronized boolean isDone() {
    return this.didComplete;
  }

  @Override
  public synchronized byte[] get() throws InterruptedException, EntityException {
    while (!this.didComplete) {
      this.wait();
    }
    if (null != this.error) {
      throw this.error;
    }
    return this.response;
  }

  @Override
  public byte[] getWithTimeout(long timeout, TimeUnit unit) throws InterruptedException, EntityException, TimeoutException {
    throw new IllegalStateException("Not supported");
  }

  public synchronized void sent() {
    this.waitingForSent = false;
    notifyAll();
  }

  public synchronized void handleAck() {
    this.waitingForReceive = false;
    notifyAll();
  }

  public synchronized void handleComplete(byte[] result, EntityException error) {
    this.waitingForComplete = false;
    this.didComplete = true;
    this.response = result;
    this.error = error;
    notifyAll();
  }

  public void saveRawMessageForResend(byte[] raw) {
    this.rawMessageForResend = raw;
  }

  /**
   * Resets the ACK wait state for the receiver and returns the raw message for the caller to re-send.
   */
  public byte[] resetAndGetMessageForResend() {
    this.waitingForReceive = this.shouldWaitForReceived;
    this.waitingForComplete = this.shouldWaitForCompleted;
    this.didComplete = false;
    this.response = null;
    this.error = null;
    return this.rawMessageForResend;
  }
}
