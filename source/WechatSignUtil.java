
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

/**
 * 微信支付工具类
 */
public class WechatSignUtil {

    public static String getSign(Map<String, String> params, String paternerKey)
        throws UnsupportedEncodingException {
        return Md5Util.MD5Encode(createSign(params, false) + "&key=" + paternerKey).toUpperCase();
    }

    /**
     * 构造签名
     */
    public static String createSign(Map<String, String> params, boolean encode)
        throws UnsupportedEncodingException {
        Set<String> keysSet = params.keySet();
        Object[] keys = keysSet.toArray();
        Arrays.sort(keys);
        StringBuffer temp = new StringBuffer();
        boolean first = true;
        for (Object key : keys) {
            // 参数为空不参与签名
            if (key == null || StringUtils.isBlank(params.get(key))) {
                continue;
            }
            if (first) {
                first = false;
            } else {
                temp.append("&");
            }
            temp.append(key).append("=");
            Object value = params.get(key);
            String valueStr = "";
            if (null != value) {
                valueStr = value.toString();
            }
            if (encode) {
                temp.append(URLEncoder.encode(valueStr, "UTF-8"));
            } else {
                temp.append(valueStr);
            }
        }
        return temp.toString();
    }

}
