package org.message.transfer.util;

import java.nio.ByteBuffer;

public class BufferUtil {

    public static String decode(ByteBuffer buffer){
        //Note: Don't use buffer.array()
        //Because buffer.clear() would not actually clear the buffer, just move the limit, buffer.array is not accurate
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return new String(bytes);
    }
}
