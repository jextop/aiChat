package com.walle.util;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MacUtil {
    private static String macAddress = null;

    public static String gtMacAddr() {
        if (macAddress == null) {
            synchronized (MacUtil.class) {
                if (macAddress == null) {
                    try {
                        Set<String> macSet = getMacSet();
                        Iterator<String> iterator = macSet.iterator();
                        if (iterator.hasNext()) {
                            macAddress = iterator.next();
                        }
                    } catch (SocketException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
        return macAddress;
    }

    private static Set<String> getMacSet() throws SocketException {
        Set<String> macSet = new HashSet<String>();

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            List<InterfaceAddress> addresses = networkInterface.getInterfaceAddresses();
            for (InterfaceAddress address : addresses) {
                InetAddress ip = address.getAddress();
                NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                if (network == null) {
                    continue;
                }

                byte[] mac = network.getHardwareAddress();
                if (mac == null) {
                    continue;
                }

                // 字节转成字符
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                macSet.add(sb.toString());
            }
        }

        return macSet;
    }
}
