package org.cen.ui.gameboard.elements.trajectory;

import java.awt.geom.GeneralPath;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GaugeFactory {
	public Gauge getGauge(InputStream stream) {
		GeneralPath path = new GeneralPath();

		InputStreamReader reader = new InputStreamReader(stream);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("path", path);
		try {
			engine.eval(reader, bindings);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Gauge gauge = new Gauge(path);
		return gauge;
	}
}
