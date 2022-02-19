package org.message.transfer.endpoint;

import java.io.Closeable;
import java.io.IOException;

public interface Server extends Closeable {
    void start() throws IOException;

}
