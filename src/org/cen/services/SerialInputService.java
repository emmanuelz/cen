package org.cen.services;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;

public class SerialInputService {
	public SerialInputService(String port, final InputParser parser) {
		super();
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);
			if (portIdentifier.isCurrentlyOwned()) {
				System.out.println("Error: Port is currently in use");
				return;
			}

			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

			if (!(commPort instanceof SerialPort)) {
				System.out.println("Error: Only serial ports are handled by this example.");
				return;
			}

			SerialPort serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			final InputStream in = serialPort.getInputStream();

			Thread inThread = new Thread(new Runnable() {
				@Override
				public void run() {
					byte[] buffer = new byte[1024];
					int len = -1;
					try {
						while ((len = in.read(buffer)) > -1) {
							parser.parse(buffer, len);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			inThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
