package com.rpm.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtils {


    /**
     * 获取连接的IP
     * @param address
     * @return
     */
    public static String getIp(String address){
        return address.substring(0, address.lastIndexOf(":"));
    }

    /**
     * 判断IP是否为回环地址
     * @param address
     * @return
     */
    public static boolean isLoopback(String address) {
        return address.equals("localhost") || address.equals("127.0.0.1") || address.equals("::1") || address.equals("::") || address.equals("0.0.0.0");
    }

    /**
     * 判断是否为任意IP
     * @param address
     * @return
     */
    public static boolean isAnyAddress(String address) {
        return address == null || address.equals("0.0.0.0") || address.equals("::");
    }

    /**
     * 获取连接的端口
     * @param address
     * @return
     */
    public static String getPort(String address){
        return address.substring(address.lastIndexOf(":")+1);
    }

    /**
     * 判断IP是否为内网地址
     * @param ip
     * @return
     */
    public static boolean isInternalIp(String ip) {
        if(isLoopback(ip)){
            return true;
        }
        // A类  10.0.0.0-10.255.255.255
        long aBegin = 167772160L;
        long aEnd = 184549375L;
        // B类  172.16.0.0-172.31.255.255
        long bBegin = 2886729728L;
        long bEnd = 2887778303L;
        // C类  192.168.0.0-192.168.255.255
        long cBegin = 3232235520L;
        long cEnd = 3232301055L;

        long t = getIpNum(ip);

        return isBetween(t, aBegin, aEnd) || isBetween(t, bBegin, bEnd) || isBetween(t, cBegin, cEnd);
    }

    private static boolean isBetween(long ip, long begin, long end){
        return (ip >=begin) && (ip <= end);
    }

    private static long getIpNum(String ip) {
        byte[] bytes = new byte[0];
        try {
            bytes = InetAddress.getByName(ip).getAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, bytes).longValue();
    }
}
