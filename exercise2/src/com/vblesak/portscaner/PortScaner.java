/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vblesak.portscaner;

import com.vblesak.portscaner.host.Host;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Viktor
 */
public class PortScaner {

    private static final String PARAM_LONG_THREADS = "threads";
    private static final String PARAM_SHORT_THREADS = "p";
    private static final String PARAM_LONG_PORT_START = "minPort";
    private static final String PARAM_SHORT_PORT_START = "s";
    private static final String PARAM_LONG_PORT_END = "maxPort";
    private static final String PARAM_SHORT_PORT_END = "e";
    private static final String PARAM_LONG_TIMEOUT = "timeout";
    private static final String PARAM_SHORT_TIMEOUT = "t";
    private static final String PARAM_LONG_HELP = "help";
    private static final String PARAM_SHORT_HELP = "h";
    
    private static final String SERIAL_FILE_NAME = "hostMap.dat";
    
    private static final int DEF_THREAD_COUNT = 4;
    private static final int DEF_TIMEOUT = 50;
    private static final int PORT_WK_START = 1;
    private static final int PORT_WK_END = 1023;
    private static final int PORT_REG_START = 1024;
    private static final int PORT_REG_END = 49151;
    private static final int PORT_DYN_START = 49152;
    private static final int PORT_DYN_END = 65535;
    private static final int PORT_DEF_START = PORT_WK_START;
    private static final int PORT_DEF_END = PORT_WK_END;
    
    public static void main(String[] args) {
        
        Options options = new Options();
        options.addOption(PARAM_SHORT_THREADS, PARAM_LONG_THREADS, true, "Number of threads used during scanning. Default value = " + DEF_THREAD_COUNT);
        options.addOption(PARAM_SHORT_PORT_START, PARAM_LONG_PORT_START, true, "Port number where scanning starts. Default value = " + PORT_DEF_START);
        options.addOption(PARAM_SHORT_PORT_END, PARAM_LONG_PORT_END, true, "Port number where scanning stops. Default value = " + PORT_DEF_END);
        options.addOption(PARAM_SHORT_TIMEOUT, PARAM_LONG_TIMEOUT, true, "Timeout after which is socket considered closed. Default value = " + DEF_TIMEOUT);
        options.addOption(PARAM_SHORT_HELP, PARAM_LONG_HELP, false, "Shows this information");
        
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
             cmd = parser.parse( options, args);
        } catch (ParseException ex) {
            System.out.println("Parsing failed:\n" + ex.getMessage());
            return;
        }
        
        if(cmd.hasOption(PARAM_LONG_HELP)){
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "portScaner", options );
            return;
        }
        int threadCount = getIntOption(cmd, PARAM_LONG_THREADS, DEF_THREAD_COUNT);
        int portStart = getIntOption(cmd, PARAM_LONG_PORT_START, PORT_DEF_START);
        int portEnd = getIntOption(cmd, PARAM_LONG_PORT_END, PORT_DEF_END);
        int timeout = getIntOption(cmd, PARAM_LONG_TIMEOUT, DEF_TIMEOUT);
        
        // deserialibize
        Map<String, Host> hostsMap = deserialize(SERIAL_FILE_NAME);
        
        
        for(String hostIp : cmd.getArgList()){
            Host host = hostsMap.get(hostIp);
            boolean newHosts = false;
            if(host == null){
                newHosts = true;
                try {
                    host = Host.getHost(hostIp);
                } catch (UnknownHostException ex){
                    System.out.println("Unknown host " + hostIp + "\n" + ex.getMessage());
                }
            }
            host.reset();
            host.scanRange(portStart, portEnd, timeout, threadCount);
            
            if(newHosts){
                hostsMap.put(hostIp, host);
            }
        }
        serialize(hostsMap, SERIAL_FILE_NAME);
    }
    
    private static int getIntOption(CommandLine cmd, String parameterName, int defaultValue){
        int retVal = defaultValue;
        try {
            retVal = cmd.hasOption(parameterName) ? Integer.parseInt(cmd.getOptionValue(parameterName)) : defaultValue;
        } catch (Exception e){
            System.out.println("Parsing of parameter " + parameterName + "failed. Assigning default value " + defaultValue);
        }
        return retVal;
    }
    
    private static void serialize(Map<String, Host> hostsMap, String fileName){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(hostsMap);
            out.close();
        } catch (IOException ex){
        }
    }
    
    private static Map<String, Host> deserialize(String fileName){
        Map<String, Host> retval;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            retval = (HashMap) in.readObject();
            in.close();
        } catch (Exception ex){
            retval = new HashMap();
        }
        return retval;
    }
}
