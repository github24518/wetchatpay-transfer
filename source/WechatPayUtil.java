
import com.coding.happy.go.common.constants.AlipayTransferStatusConstants;
import com.coding.happy.go.common.model.AlipayTransferStatusBo;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_2
 */
@Slf4j
@Component
public class WechatPayUtil {

    @Value("${wx.mchid:}")
    private String MCH_ID;
    @Value("${wx.secret:}")
    private String API_SECRET;
    @Value("${wx.transfer_url:}")
    private String TRANSFER_URL;
    @Value("${wx.spbill_create_ip:}")
    private String SPBILL_CREATE_IP;

    static final BigDecimal NUM100 = new BigDecimal(100);

    /**
     * 微信企业付款
     */
    public void transfer(String orderNo, String openid, String realName,
        BigDecimal amount, String transferRemark,String wechatAppid)
        throws Exception {
        Map<String, String> param = new HashMap<>();
        //公众账号appid
        param.put("mch_appid", wechatAppid);
        //商户号
        param.put("mchid", MCH_ID);
        //随机字符串
        param.put("nonce_str", UUIDUitl.generateString(32));
        //商户订单号
        param.put("partner_trade_no", orderNo);
        //用户openid
        param.put("openid", openid);
        //校验用户姓名选项 NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名
        param.put("check_name", "FORCE_CHECK");
        //姓名
        param.put("re_user_name", realName);
        //转账金额,单位为分
        param.put("amount", amount.multiply(NUM100).intValue() + "");
        //企业付款描述信息
        param.put("desc", transferRemark);
        //Ip地址 该IP同在商户平台设置的IP白名单中的IP没有关联，该IP可传用户端或者服务端的IP。
        param.put("spbill_create_ip", SPBILL_CREATE_IP);
        //签名
        param.put("sign", WechatSignUtil.getSign(param, API_SECRET));
        String reqxml = WechatXmlUtil.xmlFormat(param, false);
        log.info("微信转账|请求参数:{}", reqxml);
        String restxml = WechatHttpsUtil.posts(TRANSFER_URL, reqxml);
        log.info("微信转账|返回参数:{}", reqxml);
        Map<String, String> resmap = WechatXmlUtil.xmlParse(restxml);
        String result_code = resmap.get("result_code");
        String return_msg = resmap.get("return_msg");
        String err_code = resmap.get("err_code");
        String err_code_des = resmap.getOrDefault("err_code_des", "");
        String payment_no = resmap.getOrDefault("payment_no", "");
        String resMsg = StringUtils.isNotBlank(err_code_des) ? err_code_des : return_msg;
        //转账成功
        if ("SUCCESS".equals(result_code)) {
           // TODO 业务处理
            return
        }
        //待确认错误码: 系统繁忙、付款错误
        if ("SYSTEMERROR".equals(err_code) || "SEND_FAILED".equals(err_code)) {
            // TODO 业务处理
            return
        }
        //转账失败 TODO 业务处理
        return ;
    }

}
