/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package netmonitoring;

/**
 *
 * @author Ronney
 */
public class Device {
    private String hostname,ip;


    public Device(String hostname, String ip) {
        this.hostname = hostname;
        this.ip = ip;
    }
    

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Device{" + "hostname=" + hostname + ", ip=" + ip + '}';
    }

   
    
    
    
}
