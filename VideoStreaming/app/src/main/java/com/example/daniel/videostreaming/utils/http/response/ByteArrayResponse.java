package com.example.daniel.videostreaming.utils.http.response;

/**
 * @author Leonardo Pires
 */

public class ByteArrayResponse extends AbstractRequestResponse<byte[]> {


    public ByteArrayResponse(String url, int code, byte[] content) {
        super(url, code, content);
    }

    @Override
    public long getContentSize() {
        return getContent().length;
    }
}
