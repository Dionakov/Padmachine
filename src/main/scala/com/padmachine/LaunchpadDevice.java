package org.neotouch.neopad.view;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import org.neotouch.neopad.model.AbstractModel;
import org.neotouch.neopad.model.LaunchpadDeviceModel;

import javafx.geometry.Point2D;

public class LaunchpadDevice implements ViewController, Receiver
{
	private LaunchpadDeviceModel model = null;
	private Receiver deviceOut = null;
	// TODO communicate with the device.

	public void setDeviceOut(MidiDevice out) throws MidiUnavailableException
	{
		this.deviceOut = out.getReceiver();
	}

	public void linkToDeviceIn(MidiDevice in) throws MidiUnavailableException
	{
		in.getTransmitter().setReceiver(this);
	}

	public void makeUserChooseDevices() throws MidiUnavailableException
	{
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();

		System.out.println("Liste des périphériques MIDI connectés : ");

		int i = 0;
		for (MidiDevice.Info deviceInfo : devices) {
			System.out.println("Device #" + i + ": " + deviceInfo.getName());
			i++;
		}

		System.out.println();

		BufferedReader br = new BufferedReader(
				new InputStreamReader(System.in));
		int inputDeviceId = -1;
		while (inputDeviceId <= -1 || inputDeviceId >= i) {
			System.out.print("Entrez l'ID du périphérique d'ENTREE : ");

			try {
				String userInput = br.readLine();
				inputDeviceId = Integer.parseInt(userInput);
			} catch (NumberFormatException e) {
				inputDeviceId = -1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		MidiDevice inputDevice = null;

		inputDevice = MidiSystem.getMidiDevice(devices[inputDeviceId]);
		inputDevice.open();

		if (!inputDevice.isOpen()) {
			System.out.println("Impossible d'ouvrir le périphérique.");
		}

		int outputDeviceId = -1;
		while (outputDeviceId <= -1 || outputDeviceId >= i) {
			System.out.print("Entrez l'ID du périphérique de SORTIE : ");

			try {
				String userInput = br.readLine();
				outputDeviceId = Integer.parseInt(userInput);
			} catch (NumberFormatException e) {
				outputDeviceId = -1;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		MidiDevice outputDevice = null;

		outputDevice = MidiSystem.getMidiDevice(devices[outputDeviceId]);
		outputDevice.open();

		if (!outputDevice.isOpen()) {
			System.out.println("Impossible d'ouvrir le périphérique.");
		}
		System.out.println();

		setDeviceOut(outputDevice);
		linkToDeviceIn(inputDevice);
		resetDevice();
	}

	public void resetDevice()
	{
		byte[] msg = new byte[] { (byte) 0xF0, 0x00, 0x20, 0x29, 0x02, 0x18,
				0x22, 0x00, (byte) 0xF7 };
		try {
			deviceOut.send(new SysexMessage(msg, msg.length), -1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (!(arg0 instanceof LaunchpadDeviceModel) || arg1 == null) {
			return;
		}

		Point2D buttonCoords = (Point2D) arg1;
		try {
			updateButton((int) buttonCoords.getX(), (int) buttonCoords.getY());
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}

	}

	private void updateButton(int row, int col) throws InvalidMidiDataException
	{
		Color c = (model.isButtonLedOn(row, col))
				? model.getButtonColor(row, col) : Color.BLACK;
		byte buttonPos = 0;
		if (row == 0) {
			buttonPos = (byte) (104 + col);
		} else {
			buttonPos = (byte) ((9 - row) * 10 + col + 1);
		}

		byte[] msg = new byte[] { (byte) 0xF0, 0x00, 0x20, 0x29, 0x02, 0x18,
				0x0B, buttonPos, (byte) (c.getRed() / (255d / 63d)),
				(byte) (c.getGreen() / (255d / 63d)),
				(byte) (c.getBlue() / (255d / 63d)) };
		if (row != 0) {
			deviceOut.send(new SysexMessage(msg, msg.length), -1);
			deviceOut.send(new SysexMessage(msg, msg.length), -1);
		}
	}

	@Override
	public void addModel(AbstractModel model)
	{
		if (model instanceof LaunchpadDeviceModel) {
			this.model = (LaunchpadDeviceModel) model;
			this.model.addObserver(this);
		}
	}

	@Override
	public void close()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void send(MidiMessage message, long timeStamp)
	{
		System.out.println(((ShortMessage) message).getStatus());
	}

}
