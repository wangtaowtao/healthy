package com.wtao.healthy.util;



import com.alibaba.fastjson.JSON;
import com.wtao.healthy.util.sms.ChuangLanSmsUtil;
import com.wtao.healthy.util.sms.request.SmsSendRequest;
import com.wtao.healthy.util.sms.response.SmsSendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class SendMsgUtil {

	private static Logger _log = LoggerFactory.getLogger(SendMsgUtil.class);
	
	public static final String charset = "utf-8";
	// 用户平台API账号(非登录账号,示例:N1234567)
	public static String account = "N5124510";
	// 用户平台API密码(非登录密码)
	public static String pswd = "XQ3BdV6mkMb04a";

	
	public static void send(String mobile,String content) {
		//请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		// 短信内容
		String msg = "您的验证码为：" + content + "";
		//手机号码
		String phone = mobile;
		//状态报告
		String report= "true";

		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);

		String requestJson = JSON.toJSONString(smsSingleRequest);

		//_log.info("before request string is: " + requestJson);

		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);

		//_log.info("response after request result is :" + response);

		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
		System.out.println(smsSingleResponse);
		//_log.info("response  toString is :" + smsSingleResponse);
	}
	
	public static void main(String[] args) throws InterruptedException {
		mySend("13426194436", "你好");
	}

	public static void mySend(String mobile,String content) {
		//请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
		String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
		// 短信内容
		String msg = content;
		//手机号码
		String phone = mobile;
		//状态报告
		String report= "true";

		SmsSendRequest smsSingleRequest = new SmsSendRequest(account, pswd, msg, phone,report);
		String requestJson = JSON.toJSONString(smsSingleRequest);
		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
	}
	


}

