
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class WechatXmlUtil {

    private static final String PREFIX_XML = "<xml>";

    private static final String SUFFIX_XML = "</xml>";

    private static final String PREFIX_CDATA = "<![CDATA[";

    private static final String SUFFIX_CDATA = "]]>";

    /**
     * 转化成xml, 单层无嵌套
     */
    public static String xmlFormat(Map<String, String> parm, boolean isAddCDATA) {

        StringBuffer strbuff = new StringBuffer(PREFIX_XML);
        for (Entry<String, String> entry : parm.entrySet()) {
            strbuff.append("<").append(entry.getKey()).append(">");
            if (isAddCDATA) {
                strbuff.append(PREFIX_CDATA);
                if (StringUtils.isNotBlank(entry.getValue())) {
                    strbuff.append(entry.getValue());
                }
                strbuff.append(SUFFIX_CDATA);
            } else {
                if (StringUtils.isNotBlank(entry.getValue())) {
                    strbuff.append(entry.getValue());
                }
            }
            strbuff.append("</").append(entry.getKey()).append(">");
        }
        return strbuff.append(SUFFIX_XML).toString();
    }

    /**
     * 解析xml
     */
    public static Map<String, String> xmlParse(String xml)
        throws XmlPullParserException, IOException {
        Map<String, String> map = null;
        if (StringUtils.isNotBlank(xml)) {
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
            // 为xml设置要解析的xml数据
            pullParser.setInput(inputStream, "UTF-8");
            int eventType = pullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        map = new HashMap<>();
                        break;
                    case XmlPullParser.START_TAG:
                        String key = pullParser.getName();
                        if (key.equals("xml")) {
                            break;
                        }
                        String value = pullParser.nextText().trim();
                        map.put(key, value);
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = pullParser.next();
            }
        }
        return map;
    }
}
