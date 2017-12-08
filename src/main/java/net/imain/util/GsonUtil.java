package net.imain.util;

import com.google.gson.Gson;
import net.imain.common.HandlerCheck;

/**
 * @author: uncle
 * @apdateTime: 2017-12-08 11:25
 */
public class GsonUtil {
    private static Gson GSON;

    public static Gson getGSON() {
        if (HandlerCheck.ObjectIsEmpty(GSON)) {
            GSON = new Gson();
        }
        return GSON;
    }
}
