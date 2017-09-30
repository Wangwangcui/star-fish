package com.luangeng.support;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by LG on 2017/9/26.
 */
public class IPUtil {

    public static String getLoaclIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.print(IPUtil.getLoaclIP());
    }
}
