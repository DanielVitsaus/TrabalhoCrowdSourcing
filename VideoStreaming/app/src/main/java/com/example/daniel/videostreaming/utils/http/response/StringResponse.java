package com.example.daniel.videostreaming.utils.http.response;

/**
 * @author Leonardo Pires
 */

public class StringResponse extends AbstractRequestResponse<String> {

    public StringResponse(String url, int code, String content) {
        super(url, code, content);
    }

    @Override
    public long getContentSize() {
        return getContent().getBytes().length;
    }
}
