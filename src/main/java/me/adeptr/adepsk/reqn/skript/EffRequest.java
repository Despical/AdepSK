package me.adeptr.adepsk.reqn.skript;

import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;
import me.adeptr.adepsk.reqn.HttpResponse;

public class EffRequest extends Effect {

	static {
		Skript.registerEffect(EffRequest.class,
			"send [a[n]] [http] [%-string%] [web] request to [the] [url] %string% [with " +
			"(0¦[the] header[s] %-strings% [and [the] body %-strings%]" +
			"|1¦[the] body %-strings% [and [the] header[s] %-strings%])]");
	}

	static HttpResponse lastResponse;

	private static final Pattern HEADER = Pattern.compile("(.*?):(.+)");
	private static final String[] EMPTY_STRING_ARRAY = new String[0];
	private static final Field DELAYED;

	static {
		Field _DELAYED = null;
		try {
			_DELAYED = Delay.class.getDeclaredField("delayed");
			_DELAYED.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			Skript.warning("Skript's 'delayed' method could not be resolved. Some Skript warnings may " + "not be available.");
		}
		DELAYED = _DELAYED;
	}

	private static final ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private Expression<String> method;
	private Expression<String> url;
	private Expression<String> headers;
	private Expression<String> body;

	@Override
	protected void execute(Event e) {
		CompletableFuture.supplyAsync(() -> sendRequest(e), threadPool).whenComplete((resp, err) -> {
			if (err != null) {
				err.printStackTrace();
				lastResponse = null;
				return;
			}

			Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
				lastResponse = resp;
				if (getNext() != null) {
					TriggerItem.walk(getNext(), e);
				}
			});
		});
	}

	@Override
	protected TriggerItem walk(Event e) {
		debug(e, true);
		delay(e);
		execute(e);
		return null;
	}

	@SuppressWarnings("unchecked")
	private void delay(Event e) {
		if (DELAYED != null) {
			try {
				((Set<Event>) DELAYED.get(null)).add(e);
			} catch (IllegalAccessException ignored) {
			}
		}
	}

	private HttpResponse sendRequest(Event e) {
		String method = null;
		if (this.method != null) {
			method = this.method.getSingle(e).toUpperCase();
		}

		String url = this.url.getSingle(e);
		if (url == null) {
			return null;
		}
		url = url.replace('§', '&');

		String[] headers = EMPTY_STRING_ARRAY;
		if (this.headers != null) {
			headers = this.headers.getAll(e);
		}
		String body = "";
		if (this.body != null) {
			body = String.join("\n", this.body.getAll(e));
		}

		HttpURLConnection conn = null;

		try {
			URL target = new URL(url);
			conn = (HttpURLConnection) target.openConnection();

			conn.setRequestProperty("User-Agent", String.format("Reqn/%s (https://github.com/btk5h/reqn)", Main.getInstance().getDescription().getVersion()));

			for (String header : headers) {
				Matcher headerMatcher = HEADER.matcher(header);
				if (headerMatcher.matches()) {
					conn.setRequestProperty(headerMatcher.group(1).trim(), headerMatcher.group(2).trim());
				} else {
					Skript.warning(String.format("Malformed header during request to %s: %s", url, header));
				}
			}
			conn.setUseCaches(false);
			if (method != null && !method.equals("GET")) {
				conn.setRequestProperty("Content-Length", Integer.toString(body.getBytes().length));
				conn.setRequestMethod(method);
				conn.setDoOutput(true);
				try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8)) {
					out.write(body);
				}
			} else if (!body.equals("")) {
				Skript.warning("Get requests should not have a body");
			}

			String statusLine = conn.getHeaderField(0);
			Map<String, String> responseHeaders = conn.getHeaderFields().entrySet().stream().filter(h -> h.getKey() != null).collect(toMap(Map.Entry::getKey, entry -> entry.getValue().get(0)));
			InputStream response = conn.getErrorStream();

			if (response == null) {
				response = conn.getInputStream();
			}
			
			String encoding = conn.getContentEncoding();
			if (encoding != null)
				if (encoding.equalsIgnoreCase("gzip")) {
					response = new GZIPInputStream(response);
				} else if (encoding.equalsIgnoreCase("deflate")) {
					response = new InflaterInputStream(response, new Inflater(true));
				}
			StringBuilder responseBody = new StringBuilder();

			try (BufferedReader br = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8))) {
				String line;
				while ((line = br.readLine()) != null) {
					responseBody.append(line);
					responseBody.append("\n");
				}
			}

			return new HttpResponse(conn.getResponseCode(), conn.getResponseMessage(), statusLine, responseHeaders, responseBody.toString());
		} catch (MalformedURLException err) {
			Skript.warning("Tried to send a request to a malformed URL: " + url);
		} catch (IOException err) {
			err.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "send http request";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed,
			SkriptParser.ParseResult parseResult) {
		method = (Expression<String>) exprs[0];
		url = (Expression<String>) exprs[1];
		switch (parseResult.mark) {
		case 0:
			headers = (Expression<String>) exprs[2];
			body = (Expression<String>) exprs[3];
			break;
		case 1:
			body = (Expression<String>) exprs[4];
			headers = (Expression<String>) exprs[5];
			break;
		}
		return true;
	}
}