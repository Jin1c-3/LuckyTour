package com.luckytour.server.common.aoplogger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.http.HttpMethod;

import java.io.IOException;

/**
 * @author qing
 * @date Created in 2024/2/17 11:00
 */
public class HttpMethodSerializer extends StdSerializer<HttpMethod> {

	public HttpMethodSerializer() {
		super(HttpMethod.class);
	}

	@Override
	public void serialize(HttpMethod value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null) {
			gen.writeNull();
		} else {
			gen.writeString(value.name());
		}
	}
}