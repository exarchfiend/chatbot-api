package fun.mjauto.chatbot.api.domain.wx.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fun.mjauto.chatbot.api.domain.wx.model.dto.WeChatCallbackEvent;
import fun.mjauto.chatbot.api.domain.wx.utils.aes.AesException;
import fun.mjauto.chatbot.api.domain.wx.utils.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.StringReader;

@RestController
@RequestMapping("/weChat")
public class WeChatController {

    private final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    // 用于生成签名校验回调请求的合法性
    @Value("${chatBot-api.token}")
    String sToken;
    // 用于解密回调消息内容对应的密文
    @Value("${chatBot-api.corpID}")
    String sCorpID;
    // 企业ID
    @Value("${chatBot-api.encodingAESKey}")
    String sEncodingAESKey;

    /**
     * 验证回调URL
     * 企业开启回调模式时，企业微信会向验证url发送一个get请求,此方法用于接收该请求
     *
     * @param sVerifyMsgSig    消息体签名
     * @param sVerifyTimeStamp 时间戳
     * @param sVerifyNonce     随机数字串
     * @param sVerifyEchoStr   随机加密字符串
     * @return 解密出的echostr原文
     * @throws AesException 如果发生某种异常，会抛出AesException
     */
    @GetMapping("/callback")
    public String verifyUrl(@RequestParam("msg_signature") String sVerifyMsgSig,
                            @RequestParam("timestamp") String sVerifyTimeStamp,
                            @RequestParam("nonce") String sVerifyNonce,
                            @RequestParam("echostr") String sVerifyEchoStr) throws AesException {
        // 将开发者设置好的Token,EncodingAESKey和CorpID传给加解密类WXBizJsonMsgCrypt
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        // 需要返回的明文
        String sEchoStr = null;
        try {
            // 验证URL
            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            logger.error("验证回调URL发生错误：", e);
        }
        // 验证URL成功，将sEchoStr返回
        logger.info("验证回调URL返回的明文:" + sEchoStr);
        return sEchoStr;
    }

    /**
     * 回调事件
     * 用户回复消息或者点击事件响应时，企业会收到回调消息，此消息是经过企业微信加密之后的密文以post形式发送给企业,此方法用于接收该回调消息
     *
     * @param sReqMsgSig    消息体签名
     * @param sReqTimeStamp 时间戳
     * @param sReqNonce     随机数字串
     * @param sReqData      回调消息
     * @throws AesException 如果发生某种异常，会抛出AesException
     */
    @PostMapping("/callback")
    public void callback(@RequestParam("msg_signature") String sReqMsgSig,
                         @RequestParam("timestamp") String sReqTimeStamp,
                         @RequestParam("nonce") String sReqNonce,
                         @RequestBody String sReqData) throws AesException {
        // 将开发者设置好的Token,EncodingAESKey和CorpID传给加解密类WXBizJsonMsgCrypt
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        try {
            // 检验消息的真实性，并且获取解密后的明文.
            String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
            logger.info("回调数据包:" + sMsg);

            // 解析出明文xml标签的内容进行处理
            // 创建XmlMapper对象
            XmlMapper xmlMapper = new XmlMapper();
            // 将XML字符串解析为Java对象
            WeChatCallbackEvent weChatCallbackEvent = xmlMapper.readValue(sMsg, WeChatCallbackEvent.class);
            logger.info("回调数据包解析出的明文:" + weChatCallbackEvent);
            System.out.println(weChatCallbackEvent);
        } catch (Exception e) {
            logger.error("回调事件发生错误：", e);
        }
    }
}
