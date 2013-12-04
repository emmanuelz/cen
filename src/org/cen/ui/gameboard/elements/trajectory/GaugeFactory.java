package org.cen.ui.gameboard.elements.trajectory;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class GaugeFactory {
	public Shape getGauge(String filePath) throws FileNotFoundException {
		GeneralPath path = new GeneralPath();

		FileReader reader = new FileReader(filePath);

		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");
		Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("path", path);
		try {
			engine.eval(reader, bindings);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return path;
	}
}
