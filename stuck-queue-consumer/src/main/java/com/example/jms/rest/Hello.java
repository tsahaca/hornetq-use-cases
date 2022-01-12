package com.example.jms.rest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.jms.core.JmsTemplate;

import com.example.jms.controller.HandlerLookup;
import com.example.jms.message.AtfMessage;

@Path("/v1")
public class Hello {

	
    @Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @GET
	public Response getCarparkCapacity(@Context HttpServletRequest request, @QueryParam("count") Integer count,
			@QueryParam("queue") String queueName, @QueryParam("sleep") Long sleepTime,
			@QueryParam("name") String name, @QueryParam("sendToBd") String sendToBdQueue) {
		Map<String, Object> result = new HashMap<>();
		result.put("result", "OK");
		result.put("count", count);
		boolean sendToBd = "true".equals(sendToBdQueue);
		switch (queueName) {
		case "atf":
			JmsTemplate template = (JmsTemplate) HandlerLookup.getInstance().getBean("ATFJmsTemplate");
			for(int i=0;i<count;i++){
				AtfMessage atf = new AtfMessage("("+i+")th of ("+name+")", "queue/ATFQueue", sleepTime * (1000L), sendToBd);
				template.convertAndSend(atf.getQueueName(), atf);
			}
			break;
		default:
			break;
		}
		
		return Response.status(200).entity(result).build();
	}
}
