package com.faujor.web.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.faujor.entity.websocket.AricResponse;
import com.faujor.entity.websocket.WebSocketDO;
import com.faujor.service.common.WebSocketServer;

@Controller
public class WebsocketController {

	@RequestMapping("/websocket")
	public String index() {
		return "websocket/websocket";
	}

	@MessageMapping("/welcome") // 当浏览器向服务端发送请求时,通过@MessageMapping映射/welcome这个地址,类似于@ResponseMapping
	@SendTo("/topic/getResponse") // 当服务器有消息时,会对订阅了@SendTo中的路径的浏览器发送消息
	public AricResponse say(WebSocketDO webSocket) {
		try {
			// 睡眠1秒
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		AricResponse aricResponse = new AricResponse(webSocket.getSdata1(), null, "welcome!", webSocket.getType());
		return aricResponse;
	}

	@RequestMapping(value = "/pushVideoListToWeb", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody Map<String, Object> pushVideoListToWeb(@RequestBody Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			WebSocketServer.sendInfo("有新客户呼入,sltAccountId:");
			result.put("operationResult", true);
		} catch (IOException e) {
			result.put("operationResult", true);
		}
		return result;
	}
}
