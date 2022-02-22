package org.message.transfer.proton;

import org.apache.qpid.proton.engine.TransportException;
import org.apache.qpid.proton.engine.impl.TransportInput;
import org.apache.qpid.proton.engine.impl.TransportLayer;
import org.apache.qpid.proton.engine.impl.TransportOutput;
import org.apache.qpid.proton.engine.impl.TransportWrapper;

import java.nio.ByteBuffer;

public class SimpleHTTPImpl implements TransportLayer {

    private ByteBuffer inputBuffer;
    private ByteBuffer outputBuffer;


    @Override
    public TransportWrapper wrap(TransportInput input, TransportOutput output) {
        return new SimpleHTTPTransportWrapper(input, output);
    }


    private final class SimpleHTTPTransportWrapper implements TransportWrapper{
        private final TransportInput underlyingInput;
        private final TransportOutput underlyingOutput;
        private final ByteBuffer head;

        private SimpleHTTPTransportWrapper(TransportInput input, TransportOutput output) {
            underlyingInput = input;
            underlyingOutput = output;
            head = outputBuffer.asReadOnlyBuffer();
            head.limit(0);
        }

        @Override
        public int capacity() {
            return 0;
        }

        @Override
        public int position() {
            return 0;
        }

        @Override
        public ByteBuffer tail() throws TransportException {
            return null;
        }

        @Override
        public void process() throws TransportException {


        }

        @Override
        public void close_tail() {

        }

        @Override
        public int pending() {
            return 0;
        }

        @Override
        public ByteBuffer head() {
            return null;
        }

        @Override
        public void pop(int i) {

        }

        @Override
        public void close_head() {

        }
    }
}
