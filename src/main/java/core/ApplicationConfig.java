/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package core;

import util.functions;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class ApplicationConfig {
    private static final String GITEE_CONFIG_URL = "https://gitee.com/beichendram/Godzilla/raw/master/%s";
    private static final String GIT_CONFIG_URL = "https://raw.githubusercontent.com/BeichenDream/Godzilla/master/%s";
    private static String ACCESS_URL = "https://gitee.com/beichendram/Godzilla/raw/master/%s";
    public static final String GIT = "https://github.com/BeichenDream/Godzilla";
    private static final HashMap<String, String> headers = new HashMap();

//

    private static HashMap<String, String> getAppConfig(String configUrl) throws Exception {
        String[] lines;
        String configString;
        byte[] result = functions.httpReqest(configUrl, "GET", headers, null);
        if (result == null) {
            throw new Exception("readApplication Fail!");
        }
        try {
            configString = new String(result, "utf-8");
        } catch (UnsupportedEncodingException e) {
            configString = new String(result);
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String line : lines = configString.split("\n")) {
            int index = line.indexOf(58);
            if (index == -1) continue;
            hashMap.put(line.substring(0, index).trim(), line.substring(index + 1).trim());
        }
        return hashMap;
    }

    static {
        headers.put("Accept", "*/*");
        headers.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
    }
}

