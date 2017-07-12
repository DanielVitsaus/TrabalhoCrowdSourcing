package com.example.daniel.videostreaming.utils.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leonardo Pires
 */
public class PostData {

    private final HashMap<String, String> postData;

    public PostData() {
        this.postData = new HashMap<>();
    }

    public void addOrSet(String key, String value) {
        this.postData.put(key, value);
    }

    public boolean isEmpty() {
        return (this.postData.size() == 0);
    }

    public String getContent() {
        String content = "";
        for (Map.Entry<String, String> entry : this.postData.entrySet()) {
            String pairKeyValue;
            int count = 0;
            if (count < (this.postData.size()-1)) {
                pairKeyValue = entry.getKey().toLowerCase() + "=" + entry.getValue() + "&";
            } else {
                pairKeyValue = entry.getKey().toLowerCase() + "=" + entry.getValue();
            }
            content += pairKeyValue;
        }
        return content;
    }

}
