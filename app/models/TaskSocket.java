package models;

import static akka.pattern.Patterns.ask;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;

import play.Logger;
import play.libs.Akka;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.Await;
import akka.util.Duration;

public class TaskSocket extends UntypedActor{

	static Map<Long,ActorRef> rooms = new HashMap<Long, ActorRef>();
	static Map<Long,Map<String, WebSocket.Out<JsonNode>>> members = new HashMap<Long,Map<String, WebSocket.Out<JsonNode>>>();

	public static void join(final Long projectId,String username,WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) throws Exception{
		ActorRef room = null;
		if(rooms.get(projectId)==null){
			
			room = Akka.system().actorOf(new Props(TaskSocket.class));
			rooms.put(projectId, room);
		}else{
			room = rooms.get(projectId);
		}
		
		String result = (String)Await.result(ask(room,new Join(projectId,username, out), 1000), Duration.create(1, TimeUnit.SECONDS));

		if("OK".equals(result)) {

			// For each event received on the socket,
			in.onMessage(new Callback<JsonNode>() {
				public void invoke(JsonNode event) {

					String eventType = event.get("kind").getTextValue();
					if(eventType.equals("newTask")){
						Long projectId = event.get("projectId").asLong();
						ActorRef room = rooms.get(projectId);
						String task = event.get("task").asText();
						String creator =event.get("creator").asText();
						Long taskId = event.get("taskId").asLong();
						room.tell(new NewTask(task,projectId,creator,taskId));
						
					}

				} 
			});

			// When the socket is closed.
			in.onClose(new Callback0() {
				public void invoke() {
					Logger.error("close");
				}
			});

		} else {

			// Cannot connect, create a Json error.
			ObjectNode error = Json.newObject();
			error.put("error", result);

			// Send the error to the socket.
			out.write(error);

		}
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Join) {

			// Received a Join message
			Join join = (Join)message;
			// Check if this username is free.
				Map<String,WebSocket.Out<JsonNode>> who = members.get(join.projectId);
				if(who==null){
					who = new HashMap<String,WebSocket.Out<JsonNode>>();
					if(!who.containsKey(join.username)){
						who.put(join.username,join.channel);
					}
					Logger.info("adding "+join.username+" to room" + join.projectId);
					members.put(join.projectId,who);
				}
				notifyAll(join.projectId,"join", join.username, "has entered the room");
				getSender().tell("OK");
			

		}else if(message instanceof NewTask){
			NewTask newTask = (NewTask)message;
			ObjectNode event = Json.newObject();
			event.put("task",newTask.label);
			event.put("taskId", newTask.taskId);
			notifyAll(newTask.projectId,"newTask", newTask.username, event.toString());
			getSender().tell("OK");
		}
		else if(message instanceof Quit){
			Quit quit = (Quit)message;
			Map<String,WebSocket.Out<JsonNode>> m = members.get(quit.projectId);
			if(m.get(quit.username)!=null){
				m.remove(quit.username);
			}
			members.put(quit.projectId, m);
		}
	}
	
	public void notifyAll(Long projectId,String kind, String user, String text) {
        Map<String,WebSocket.Out<JsonNode>> m = members.get(projectId);
        for(String username: m.keySet()) {
        	if(!username.equals(user)){
            ObjectNode event = Json.newObject();
            event.put("kind", kind);
            event.put("user", user);
            event.put("message", text);
            ArrayNode membersArray = event.putArray("members");
            for(String u: m.keySet()) {
                membersArray.add(u);
            }
            m.get(username).write(event);
        	}
        }
    }

		public static class Join {

			final String username;
			final WebSocket.Out<JsonNode> channel;
			final Long projectId;

			public Join(Long projectId,String username, WebSocket.Out<JsonNode> channel) {
				this.username = username;
				this.projectId = projectId;
				this.channel = channel;
			}

		}
		
		public static class NewTask {
			final Long projectId;
			final String label;
			final String username;
			final Long taskId;

			public NewTask(String label,Long projectId,String username,Long taskId) {
				this.label = label;
				this.projectId = projectId;
				this.username = username;
				this.taskId = taskId;
			}

		}
		
		public static class Quit {
			final Long projectId;
			final String username;

			public Quit(Long projectId,String username) {
				
				this.projectId = projectId;
				this.username = username;
			}

		}



	}
