package com.sismics.util;

import com.sismics.docs.core.model.Auth.JWTModel;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class JWTUtilTest {
    @Test
    public void testJWTToken() throws Exception {
        var info = new HashMap<String, Object>();
        info.put("id", 12345);
        info.put("name", "YUBI<>TEEDY");
        String secretKey = "nsK0ZekLdM7pnAVe7hoQw8_mAUm4TIbA-oSReF0rKXPImvQE";
        JWTModel tokenModel = JWTUtil.generateToken(info, secretKey, 30);
        var verifiedInfo = JWTUtil.verifyToken(tokenModel.accessToken, secretKey);
        Assert.assertEquals(info.get("id"), verifiedInfo.get("id"));
        Assert.assertEquals(info.get("name"), verifiedInfo.get("name"));
    }
}
