/**
 * Copyright 2015 Jan Lolling jan.lolling@gmail.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package routines;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The routine creates a new guid based on the Mac address and the timestamp
 * @author jan.lolling@cimt-ag.de
 */
public class Guid {

    private static int        counter   = 0;
    private static final byte shiftab[] = { 0, 8, 16, 24, 32, 40, 48, 56 };

    private Guid() {}

	/**
     * nextGuid: returns a new GUID
     * @return guid
     * 
     * {talendTypes} String
     * 
     * {Category} Guid
     * 
     * {example} nextGuid() # new guid.
     */
    public synchronized static String nextGuid() {
        try {
            counter++;
            final InetAddress id = InetAddress.getLocalHost();
            final byte[] ip = id.getAddress();
            final long time = System.currentTimeMillis();
            byte[] guid = new byte[16];
            for (int i = 0; i <= 3; i++) {
                guid[i] = ip[i];
            }
            longToBytes(time, guid, 4);
            intToBytes(counter, guid, 12);
            final StringBuffer str = new StringBuffer();
            str.append(Long.toHexString(bytesToLong(guid, 0)));
            str.append(Long.toHexString(bytesToLong(guid, 8)));
            return str.toString().toUpperCase();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    private static long bytesToLong(byte[] ba, int offset) throws IllegalArgumentException {
        if (ba == null) {
            throw new IllegalArgumentException("null as byte array is not allowed");
        }
        long i = 0;
        for (int j = 0; j < 8; j++) {
            i |= ((long) ba[offset + 7 - j] << shiftab[j]) & (0xFFL << shiftab[j]);
        }
        return i;
    }

    private static void longToBytes(long i, byte[] ba, int offset) throws IllegalArgumentException {
        if (ba == null) {
            throw new IllegalArgumentException("null as byte array is not allowed");
        }
        for (int j = 0; j < 8; j++) {
            ba[offset + j] = (byte) ((i >>> shiftab[7 - j]) & 0xFF);
        }
    }

    private static void intToBytes(int i, byte[] ba, int offset) throws IllegalArgumentException {
        if (ba == null) {
            throw new IllegalArgumentException("null as byte array is not allowed");
        }
        for (int j = 0; j < 4; j++) {
            ba[offset + j] = (byte) ((i >>> shiftab[3 - j]) & 0xFF);
        }
    }

}