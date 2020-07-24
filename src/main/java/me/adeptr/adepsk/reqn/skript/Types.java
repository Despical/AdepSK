package me.adeptr.adepsk.reqn.skript;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.Map;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.yggdrasil.Fields;
import me.adeptr.adepsk.reqn.HttpResponse;

public class Types {
	
	static {
		Classes.registerClass(new ClassInfo<>(HttpResponse.class, "httpresponse").user("httpresponses?").parser(new Parser<HttpResponse>() {
					@Override
					public HttpResponse parse(String s, ParseContext context) {
						return null;
					}

					@Override
					public boolean canParse(ParseContext context) {
						return false;
					}

					@Override
					public String toString(HttpResponse o, int flags) {
						return o.toString();
					}

					@Override
					public String toVariableNameString(HttpResponse o) {
						return o.toString();
					}

					@Override
					public String getVariableNamePattern() {
						return ".*";
					}
				}).serializer(new Serializer<HttpResponse>() {
					@Override
					public Fields serialize(HttpResponse o) throws NotSerializableException {
						Fields fields = new Fields();
						fields.putPrimitive("code", o.getCode());
						fields.putObject("message", o.getMessage());
						fields.putObject("status", o.getStatusLine());
						fields.putObject("headers", o.getHeaders());
						fields.putObject("body", o.getBody());
						return fields;
					}

					@Override
					public void deserialize(HttpResponse o, Fields f)
							throws StreamCorruptedException, NotSerializableException {
						throw new UnsupportedOperationException();
					}

					@SuppressWarnings("unchecked")
					@Override
					protected HttpResponse deserialize(Fields fields)
							throws StreamCorruptedException, NotSerializableException {
						return new HttpResponse(fields.getPrimitive("code", int.class),
								fields.getObject("message", String.class), fields.getObject("status", String.class),
								fields.getObject("headers", Map.class), fields.getObject("body", String.class));
					}

					@Override
					public boolean mustSyncDeserialization() {
						return false;
					}

					@Override
					public boolean canBeInstantiated(Class<? extends HttpResponse> c) {
						return false;
					}

					@Override
					protected boolean canBeInstantiated() {
						return false;
					}
				}));
	}
}
