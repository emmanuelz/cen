package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Color;
import java.awt.geom.GeneralPath;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GaugeFactory {
	public class GaugeFactoryAPI {
		private Color color = Color.LIGHT_GRAY;

		public void setColor(long argb) {
			argb ^= 0xFF000000;
			color = new Color((int) argb, true);
		}

		public void setColor(int r, int g, int b, int a) {
			a ^= 0xFF;
			color = new Color(r, g, b, a);
		}
	}

	public Gauge getGauge(InputStream stream) {
		GeneralPath path = new GeneralPath();
		GaugeFactoryAPI api = new GaugeFactoryAPI();

		InputStreamReader reader = new InputStreamReader(stream);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("path", path);
		bindings.put("self", api);
		try {
			engine.eval(reader, bindings);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gauge gauge = new Gauge(path, api.color);
		return gauge;
	}
}
