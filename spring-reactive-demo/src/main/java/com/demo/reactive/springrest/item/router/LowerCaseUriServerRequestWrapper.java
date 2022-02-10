package com.demo.reactive.springrest.item.router;

import java.net.URI;

import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;

public class LowerCaseUriServerRequestWrapper extends ServerRequestWrapper {

	public LowerCaseUriServerRequestWrapper(ServerRequest delegate) {
		super(delegate);
	}
	
	@Override
    public URI uri() {
        return URI.create(super.uri().toString().toLowerCase());
    }

    @Override
    public String path() {
        return uri().getRawPath();
    }

    @Override
    public PathContainer pathContainer() {
        return PathContainer.parsePath(path());
    }
}
