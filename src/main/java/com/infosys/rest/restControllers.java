package com.infosys.rest;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api")
public class restControllers {
	List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	
	@GetMapping(path = "/sseEventInitiator", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sseEventInitiator() {
		return sseEventInitiatorfunc();
	}
	
	@GetMapping(path = "/sseEventEmit/{event}/{data}")
	public void sseEventEmit(@PathVariable(name="event")String event, @PathVariable(name="data")String data) {
		System.out.println(event);
		System.out.println(data);
		for (SseEmitter emitter : emitters) {
			try {

				emitter.send(SseEmitter.event().name(event).data(data));
			}

			catch (IOException e) {
				emitters.remove(emitter);
			}
		}
	}
	
	public SseEmitter sseEventInitiatorfunc() {
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		try {
			emitter.send(SseEmitter.event().name("INIT"));

		} catch (IOException e) {
			System.out.println(e);
		}
		emitter.onCompletion(() -> emitters.remove(emitter));
		emitters.add(emitter);
		return emitter;
	}
    
	public void sseEvent(String event, String data) {
		
		for (SseEmitter emitter : emitters) {
			try {

				emitter.send(SseEmitter.event().name(event).data(data));
			}

			catch (IOException e) {
				emitters.remove(emitter);
			}
		}

	}
    
	public void sseEventComplete() {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().name("close").data("COMPLETED"));
				emitter.complete();
			} catch (Exception e) {
				emitters.remove(emitter);
				System.out.println(e);
			}
		}
	}

	
}
