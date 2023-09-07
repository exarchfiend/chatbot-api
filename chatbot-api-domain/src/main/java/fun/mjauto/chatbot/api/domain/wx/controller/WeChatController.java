package fun.mjauto.chatbot.api.domain.wx.controller;

import fun.mjauto.chatbot.api.domain.wx.utils.aes.AesException;
import fun.mjauto.chatbot.api.domain.wx.utils.aes.WXBizJsonMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weChat")
public class WeChatController {

    private final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Value("${chatBot-api.token}")
    String sToken;
    @Value("${chatBot-api.corpID}")
    String sCorpID;
    @Value("${chatBot-api.encodingAESKey}")
    String sEncodingAESKey;

    /**
     * 验证回调URL
     * 企业开启回调模式时，企业微信会向验证url发送一个get请求,此方法用于接收该请求
     *
     * @param sVerifyMsgSig 消息体签名
     * @param sVerifyTimeStamp 时间戳
     * @param sVerifyNonce 随机数字串
     * @param sVerifyEchoStr 随机加密字符串
     * @return 解密出的echostr原文
     * @throws AesException 如果发生某种异常，会抛出AesException
     */
    @GetMapping("/verifyCallback")
    public String verifyCallback(@RequestParam("msg_signature") String sVerifyMsgSig,
                                 @RequestParam("timestamp") String sVerifyTimeStamp,
                                 @RequestParam("nonce") String sVerifyNonce,
                                 @RequestParam("echostr") String sVerifyEchoStr) throws AesException {
        // 将开发者设置好的Token(用于生成签名校验回调请求的合法性),EncodingAESKey(用于解密回调消息内容对应的密文)和CorpID(企业ID)传给加解密类WXBizJsonMsgCrypt
        WXBizJsonMsgCrypt wxcpt = new WXBizJsonMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        // 需要返回的明文
        String sEchoStr = null;
        try {
            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,sVerifyNonce, sVerifyEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            logger.error("验证回调URL发生错误：", e);
        }
        // 验证URL成功，将sEchoStr返回
        return sEchoStr;
    }
}
