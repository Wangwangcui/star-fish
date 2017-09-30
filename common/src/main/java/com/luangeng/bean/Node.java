package com.luangeng.bean;

/**
 * Created by LG on 2017/9/29.
 */
public class Node {

    private String ip;
    private int port;


    public Node(String ip, int port) {
        this.ip = ip;
        this.port = Integer.valueOf(port);
    }

    public Node(String address) {
        String[] array = address.split(":");
        if (array.length != 2) {
            throw new IllegalArgumentException("address error");
        }
        this.ip = array[0];
        this.port = Integer.parseInt(array[1]);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
