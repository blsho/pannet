/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vblesak.portscaner.host;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Viktor
 */
public class Host implements Runnable, Serializable {
    private final InetAddress hostIp;
    private final Map<Integer,Boolean> openPorts = Collections.synchronizedMap(new HashMap<Integer,Boolean>());
    private int threadCount = 0;
    private int portStart;
    private int portEnd;
    private int timeout;
    private int threadMax;
    private int changeCount = 0;
    
    
    private Host(InetAddress hostIp){
        this.hostIp = hostIp;
    }
    
    public static Host getHost(String ipAddress) throws UnknownHostException{
        InetAddress hostIp = InetAddress.getByName(ipAddress);
        return new Host(hostIp);
    }
    
    public void reset(){
        threadCount = 0;
        changeCount = 0;
    }
    
    public void scanRange(int portStart, int portEnd, int timeout, int threads){
        this.portStart = portStart;
        this.portEnd = portEnd;
        this.timeout = timeout;
        this.threadMax = threads;
        this.threadCount = 0;
        
        List<Thread> threadList = new ArrayList();
        for(int i=0; i<threads; i++){
            Thread thread = new Thread(this);
            threadList.add(thread);
            thread.start();
        }
        for(Thread thread : threadList){
            try {
                thread.join();
            } catch (InterruptedException ex) {
            }
        }
        if(changeCount == 0){
            System.out.println("*Target - " + hostIp.getHostAddress() + ": No new records found in the last scan.*");
        }
    }
    
    private void scanRange(int portStart, int portEnd, int timeout){
        for(int port=portStart; port<=portEnd; port++){
            boolean wasOpen = openPorts.get(port) == null ? false : openPorts.get(port);
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(hostIp, port), timeout);
                socket.close();
                // opened
                if(wasOpen){
                    // no change
                } else {
                    // change
                    openPorts.put(port, true);
                    increaseChangeCount();
                    System.out.println("Host: " + hostIp.getHostAddress() + " Port: " + port + "/opened/tcp");
                }
                
            } catch (Exception ex) {
                // closed
                if(wasOpen){
                    // change
                    openPorts.remove(port);
                    increaseChangeCount();
                    System.out.println("Host: " + hostIp.getHostAddress() + " Port: " + port + "/closed/tcp");
                } else {
                    // no change
                }
            }
        }
    }
    
    private synchronized int getTicket(){
        return threadCount++;
    }
    
    private synchronized void increaseChangeCount(){
        if(changeCount == 0){
            System.out.println("*Target - " + hostIp.getHostAddress() + ": Full scan results:*");
        }
        changeCount++;
    }

    @Override
    public void run() {
        int threadNumber = getTicket();
        int threadRange = (portEnd - portStart) / threadMax;
        int threadPortStart = portStart + (threadRange * threadNumber);
        int threadPortEnd = portStart + (threadRange * (threadNumber + 1)) - 1;
        threadPortEnd = portEnd < threadPortEnd ? portEnd : threadPortEnd;
        if(threadNumber + 1 == threadMax){
            threadPortEnd = portEnd;
        }
        scanRange(threadPortStart, threadPortEnd, timeout);
    }
}
