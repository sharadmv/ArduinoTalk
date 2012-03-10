package com.sharad.android;

import com.flotype.bridge.Reference;
import com.flotype.bridge.ServiceClient;

public class PushClient extends ServiceClient {
	public PushClient(Reference reference) {
		super(reference);
	}

	public void push(String name, String message) {
		this.invokeRPC("push", name, message);
	}
}
