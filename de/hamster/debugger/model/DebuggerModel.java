package de.hamster.debugger.model;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.Bootstrap;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.ClassPrepareEvent;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventQueue;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.StepEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.event.VMStartEvent;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import com.sun.jdi.request.StepRequest;

import de.hamster.flowchart.controller.FlowchartHamster;
import de.hamster.flowchart.controller.FlowchartHamsterFile;
import de.hamster.fsm.controller.FsmHamster;
import de.hamster.fsm.controller.FsmHamsterFile;
import de.hamster.model.HamsterFile;
import de.hamster.model.InstructionProcessor;
import de.hamster.prolog.controller.PrologController;
import de.hamster.prolog.model.PrologHamster;
import de.hamster.python.model.PythonHamster;
import de.hamster.ruby.model.RubyHamster;
import de.hamster.scheme.model.SchemeHamster;
import de.hamster.scheme.view.SchemeKonsole;
import de.hamster.scratch.ScratchHamster;
import de.hamster.scratch.ScratchHamsterFile;
import de.hamster.workbench.Utils;
import de.hamster.workbench.Workbench;

/**
 * TODO: Testen der Debuggersteuerung (vor allem ThreadResumed und
 * ConnectionClosed)
 * 
 * @author $Author: djasper $
 * @version $Revision: 1.1 $
 */
public class DebuggerModel extends Observable implements Runnable {
	/**
	 * This argument informs off a state-change.
	 */
	public static final String ARG_ENABLE = "enable";

	/**
	 * This argument informs off a state-change.
	 */
	public static final String ARG_STATE = "state-change";

	/**
	 * In this state the debugger is not running. It has no associated Runner.
	 */
	public static final int NOT_RUNNING = 0;

	/**
	 * In this state the debugger is executing the user-sourcecode.
	 */
	public static final int RUNNING = 2;

	/**
	 * 
	 */
	public static final int PAUSED = 5;

	/**
	 * This is the current state of the Debugger.
	 */
	private int state;

	/**
	 * This is the HamsterFile to be debugged.
	 */
	private HamsterFile currentFile;

	/**
	 * The corresponding class name.
	 */
	private String className;

	private boolean enabled;

	private ArrayList lockedFiles;
	protected ArrayList lockedRefs;

	protected int delay;

	protected boolean suspended;

	RemoteRunner runner;

	InstructionProcessor processor;
	VirtualMachine machine;
	EventRequestManager eventRequestManager;
	EventQueue eventQueue;
	StepRequest stepRequest;
	ThreadReference suspendedThread;
	Thread thread;

	// Martin
	Thread hamsterThread;

	protected List stackFrames;

	private LocalProcessor localProcessor;

	public DebuggerModel(InstructionProcessor processor) {
		this.processor = processor;
		localProcessor = new LocalProcessor(processor, this);
		state = NOT_RUNNING;
		delay = 500;
		lockedFiles = new ArrayList();
		lockedRefs = new ArrayList();
	}

	public int getState() {
		return state;
	}

	public List getStackFrames() {
		return stackFrames;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void start(HamsterFile file) {
		if (state != NOT_RUNNING)
			// TODO: ueber Exceptions
			return;
		currentFile = file;
		if (currentFile == null)
			return;

		if (Utils.runlocally) {
			IHamster.processor = localProcessor;
			/* lego */de.hamster.lego.model.LHamster.processor = localProcessor;
			Territorium.processor = localProcessor;
			Hamster.count = 0;
			localProcessor.start();
			setState(PAUSED);
			try {
				localProcessor.run(new HamsterClassLoader()
						.getHamsterInstance(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.className = currentFile.getName();

			LaunchingConnector connector = Bootstrap.virtualMachineManager()
					.defaultConnector();
			Map args = connector.defaultArguments();
			Connector.Argument main = (Connector.Argument) args.get("main");
			main.setValue(RemoteRunner.class.getName() + " " + className);
			Connector.Argument options = (Connector.Argument) args
					.get("options");
			options.setValue("-classpath \"" + file.getDir() + Utils.PSEP
					+ System.getProperty("java.class.path") + Utils.PSEP
					+ Workbench.getWorkbench().getProperty("classpath", "")
					+ "\"");
			try {
				machine = connector.launch(args);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			setState(PAUSED);
			stepRequest = null;
			eventRequestManager = machine.eventRequestManager();
			eventQueue = machine.eventQueue();
			thread = new Thread(this);
			thread.start();
			runner = new RemoteRunner(processor, machine.process());
			if (!enabled)
				runner.setDelay(delay);
			runner.start();
		}
	}

	// Martin
	/**
	 * Eine weitere Start-Methode um einen Scheme bzw. Prolog-Hamster zu
	 * starten. Diese Methode braucht als weiteren Parameter die
	 * Workbench-Objektreferenz.
	 */
	public void start(HamsterFile file, final Workbench workbench) {
		if (state != NOT_RUNNING)
			// TODO: ueber Exceptions
			return;
		currentFile = file;
		if (currentFile == null)
			return;

		if (file.getType() == HamsterFile.SCHEMEPROGRAM) {
			// Workbench im SchemeHamster setzen
			SchemeHamster.setWorkbench(workbench);

			// Scheme-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);
			hamsterThread = new Thread() {
				public void run() {
					try {

						// dibo 260110
						JFrame simFrame = Workbench.getWorkbench().getView()
								.getSimulationFrame();
						if (!simFrame.isVisible()) {
							simFrame.setVisible(true);
							Workbench.winSim.setState(true);
						}
						simFrame.toFront();

						Workbench.getWorkbench().getSimulation()
								.getSimulationModel().start(); // dibo 210110
						// Hamster-datei laden und starten
						SchemeHamster.load(currentFile.load(), true);
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						setState(NOT_RUNNING);
						isStop = false;
						SchemeHamster.getWorkbench().getEditor()
								.getTabbedTextArea()
								.propertyChange(currentFile, false);
					}
				}
			};
			hamsterThread.start();
		}
		// Prolog
		else if (file.getType() == HamsterFile.PROLOGPROGRAM) {
			// Prolog-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);

			// Deaktiviere Tool-Buttons im Sumulationsfenster..
			Workbench.getWorkbench().getSimulation().getSimulationTools()
					.setSimulationPanelListenerEnabled(false);

			hamsterThread = new Thread() {
				public void run() {
					try {

						// dibo 260110
						JFrame simFrame = Workbench.getWorkbench().getView()
								.getSimulationFrame();
						if (!simFrame.isVisible()) {
							simFrame.setVisible(true);
							Workbench.winSim.setState(true);
						}
						simFrame.toFront();

						/*
						 * Merke sich den alten Zustand des Territoriums vor,
						 * damit die M�glichkeit des Zur�cksetzens genutzt
						 * werden kann.
						 */
						Workbench.getWorkbench().getSimulation()
								.getSimulationModel().start();
						/*
						 * �berpr�fe, ob der Prolog-Prozess noch ordnungsgem��
						 * l�uft und starte ihn gegebenfalls neu..
						 */
						PrologController.get().ensurePrologEngineIsRunning();
						/*
						 * Initialisiere den PrologHamster neu. Dabei wird die
						 * Prolog-Datenbank aufger�umt und das aktuelle
						 * Territorium neu importiert.
						 */
						PrologHamster.get().initPrologHamster();
						/*
						 * L�dt das Benutzerprogramm und startet die
						 * 'main.'-Prozedur.
						 */
						PrologHamster.get().start(currentFile);
					} catch (Exception e) {
						System.err.println(e);
					} finally {
						setState(NOT_RUNNING);
						isStop = false;
						Workbench.getWorkbench().getEditor()
								.getTabbedTextArea()
								.propertyChange(currentFile, false);
						Workbench.getWorkbench().getSimulation()
								.getSimulationTools()
								.setSimulationPanelListenerEnabled(true);
					}
				}
			};
			hamsterThread.start();
			// Python
		} else if (file.getType() == HamsterFile.PYTHONPROGRAM) {

			// Python-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);

			// Deaktiviere Tool-Buttons im Sumulationsfenster..
			// Workbench.getWorkbench().getSimulation().getSimulationTools()
			// .setSimulationPanelListenerEnabled(false);

			hamsterThread = new Thread() {
				public void run() {
					try {

						JFrame simFrame = Workbench.getWorkbench().getView()
								.getSimulationFrame();
						if (!simFrame.isVisible()) {
							simFrame.setVisible(true);
							Workbench.winSim.setState(true);
						}
						simFrame.toFront();

						Workbench.getWorkbench().getSimulation()
								.getSimulationModel().start(); // dibo 210110

						// Hamster-datei laden und starten
						PythonHamster.load(currentFile.load(), true);
					} catch (Exception e) {
						System.out.println(e);
					} finally {
						setState(NOT_RUNNING);
						isStop = false;
						Workbench.getWorkbench().getEditor()
								.getTabbedTextArea()
								.propertyChange(currentFile, false);
						// Workbench.getWorkbench().getSimulation()
						// .getSimulationTools()
						// .setSimulationPanelListenerEnabled(true);
					}
				}
			};
			hamsterThread.start();
			// Ruby
		} else if (file.getType() == HamsterFile.RUBYPROGRAM) {

			// Ruby-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);

			// Deaktiviere Tool-Buttons im Sumulationsfenster..
			// Workbench.getWorkbench().getSimulation().getSimulationTools()
			// .setSimulationPanelListenerEnabled(false);

			hamsterThread = new Thread() {
				public void run() {
					try {

						JFrame simFrame = Workbench.getWorkbench().getView()
								.getSimulationFrame();
						if (!simFrame.isVisible()) {
							simFrame.setVisible(true);
							Workbench.winSim.setState(true);
						}
						simFrame.toFront();

						Workbench.getWorkbench().getSimulation()
								.getSimulationModel().start(); // dibo 210110

						// Hamster-datei laden und starten
						RubyHamster.load(currentFile.load(), true);
					} catch (ThreadDeath th) {
					} catch (Exception e) {
						// System.out.println(e);
					} finally {
						setState(NOT_RUNNING);
						isStop = false;
						Workbench.getWorkbench().getEditor()
								.getTabbedTextArea()
								.propertyChange(currentFile, false);
						// Workbench.getWorkbench().getSimulation()
						// .getSimulationTools()
						// .setSimulationPanelListenerEnabled(true);
					}
				}
			};
			hamsterThread.start();
			// Scratch
		} else if (file.getType() == HamsterFile.SCRATCHPROGRAM) {

			// Scratch-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);

			JFrame simFrame = Workbench.getWorkbench().getView()
					.getSimulationFrame();
			if (!simFrame.isVisible()) {
				simFrame.setVisible(true);
				Workbench.winSim.setState(true);
			}
			simFrame.toFront();

			Workbench.getWorkbench().getSimulation().getSimulationModel()
					.start(); // dibo 210110

			// Hamster starten
			ScratchHamster.getScratchHamster().startProgram(
					(ScratchHamsterFile) currentFile);

		} // FSM
		else if (file.getType() == HamsterFile.FSM) {

			// FSM-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);

			JFrame simFrame = Workbench.getWorkbench().getView()
					.getSimulationFrame();
			if (!simFrame.isVisible()) {
				simFrame.setVisible(true);
				Workbench.winSim.setState(true);
			}
			simFrame.toFront();

			Workbench.getWorkbench().getSimulation().getSimulationModel()
					.start();

			// Hamster starten
			FsmHamster.getFSMHamster().startProgram(
					((FsmHamsterFile) currentFile).getProgram());

		}
		// Flowchart
		else if (file.getType() == HamsterFile.FLOWCHART) {

			// Flowchart-HamsterProgramm starten
			setState(RUNNING);
			workbench.getEditor().getTabbedTextArea()
					.propertyChange(file, true);

			JFrame simFrame = Workbench.getWorkbench().getView()
					.getSimulationFrame();
			if (!simFrame.isVisible()) {
				simFrame.setVisible(true);
				Workbench.winSim.setState(true);
			}
			simFrame.toFront();

			Workbench.getWorkbench().getSimulation().getSimulationModel()
					.start();

			// Hamster starten
			FlowchartHamster.getFlowchartHamster().startProgram(
					((FlowchartHamsterFile) currentFile).getProgram());

		}
		isStop = false;
	}

	public static boolean isStop = false;

	@SuppressWarnings("deprecation")
	public void stop() {
		// System.out.println("STOPP");

		/*
		 * Martin Falls ein Scheme-Programm gestoppt werden soll muss dies
		 * anders geschehen als bei Java-Hamstern.
		 */
		if (SchemeKonsole.isRunning) {
			SchemeKonsole.hamsterThread.stop();
			setState(NOT_RUNNING);
			// Martin + Python + Ruby
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.SCHEMEPROGRAM
						|| currentFile.getType() == HamsterFile.PYTHONPROGRAM || currentFile
						.getType() == HamsterFile.RUBYPROGRAM)) {
			isStop = true;
			try {
				hamsterThread.stop();
			} catch (ThreadDeath td) {
			}
			setState(NOT_RUNNING);
			// Unlock editor text area for editing..
			Workbench.getWorkbench().getEditor().getTabbedTextArea()
					.propertyChange(currentFile, false);
			// Scratch
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.SCRATCHPROGRAM)) {
			ScratchHamster.getScratchHamster().stopProgram();
			setState(NOT_RUNNING);
			Workbench.getWorkbench().getEditor().getTabbedTextArea()
					.propertyChange(currentFile, false);
		} // FSM
		else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FSM)) {
			FsmHamster.getFSMHamster().stopProgram();
			setState(NOT_RUNNING);
			Workbench.getWorkbench().getEditor().getTabbedTextArea()
					.propertyChange(currentFile, false);
		}
		// FSM
		else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FLOWCHART)) {
			FlowchartHamster.getFlowchartHamster().stopProgram();
			setState(NOT_RUNNING);
			Workbench.getWorkbench().getEditor().getTabbedTextArea()
					.propertyChange(currentFile, false);
		}
		// Prolog
		else if (currentFile != null
				&& currentFile.getType() == HamsterFile.PROLOGPROGRAM) {
			PrologController.get().stopPrologEngine();
			isStop = false;
			hamsterThread.stop();
			setState(NOT_RUNNING);
			// Unlock editor text area for editing..
			Workbench.getWorkbench().getEditor().getTabbedTextArea()
					.propertyChange(currentFile, false);
			Workbench.getWorkbench().getSimulation().getSimulationTools()
					.setSimulationPanelListenerEnabled(true);
		} else if (currentFile != null) {
			if (Utils.runlocally) {
				localProcessor.stop();
			} else
				machine.exit(0);
		}
	}

	public void resume() {
		if (state == PAUSED) {
			if (currentFile != null
					&& (currentFile.getType() == HamsterFile.SCRATCHPROGRAM)) {
				ScratchHamster.getScratchHamster().resumeProgram();
			} else if (currentFile != null
					&& (currentFile.getType() == HamsterFile.FSM)) {
				FsmHamster.getFSMHamster().resumeProgram();
			} else if (currentFile != null
					&& (currentFile.getType() == HamsterFile.FLOWCHART)) {
				FlowchartHamster.getFlowchartHamster().resumeProgram();
			} else {
				if (Utils.runlocally) {
					localProcessor.resume();
				} else {
					stepInto(currentFile);
				}
			}
			setState(RUNNING);
		} else {
			System.err.println("Error: resume, when not in SUSPENDED state.");
		}
	}

	public void stepOver(HamsterFile file) {
		currentFile = file;
		if (currentFile != null
				&& (currentFile.getType() == HamsterFile.SCRATCHPROGRAM)) {
			ScratchHamster.getScratchHamster().stepOver(
					(ScratchHamsterFile) currentFile);
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FSM || currentFile
						.getType() == HamsterFile.FLOWCHART)) {
			// gibts nicht
		} else {
			step(StepRequest.STEP_OVER);
		}
	}

	public void stepInto(HamsterFile file) {
		currentFile = file;
		if (currentFile != null
				&& (currentFile.getType() == HamsterFile.SCRATCHPROGRAM)) {
			ScratchHamster.getScratchHamster().stepInto(
					(ScratchHamsterFile) currentFile);
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FSM)) {
			FsmHamster.getFSMHamster().stepInto(
					((FsmHamsterFile) currentFile).getProgram());
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FLOWCHART)) {
			FlowchartHamster.getFlowchartHamster().stepInto(
					((FlowchartHamsterFile) currentFile).getProgram());
		} else {

			step(StepRequest.STEP_INTO);
		}
	}

	public void pause() {
		if (currentFile != null
				&& (currentFile.getType() == HamsterFile.SCRATCHPROGRAM || currentFile
						.getType() == HamsterFile.FSM)
				|| currentFile.getType() == HamsterFile.FLOWCHART)
			if (state == NOT_RUNNING)
				// TODO: Exceptions
				return;
		setState(PAUSED);

		// Scratch
		if (currentFile != null
				&& (currentFile.getType() == HamsterFile.SCRATCHPROGRAM)) {
			ScratchHamster.getScratchHamster().pauseProgram();
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FSM)) {
			FsmHamster.getFSMHamster().pauseProgram();
		} else if (currentFile != null
				&& (currentFile.getType() == HamsterFile.FLOWCHART)) {
			FlowchartHamster.getFlowchartHamster().pauseProgram();
		} else {
			if (Utils.runlocally) {
				localProcessor.pause();
			} else {
				if (!enabled) {
					machine.suspend();
				}
			}
		}
	}

	public void run() {
		try {
			for (;;) {
				if (removeEvent())
					break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		removeTypeLocks();
		suspended = false;
		suspendedThread = null;
		stackFrames = null;
		setState(NOT_RUNNING);
		machine = null;
	}

	public HamsterFile getProgram(ReferenceType ref) {
		String sourcePath = null;
		try {
			sourcePath = (String) ref.sourcePaths(null).get(0);
		} catch (AbsentInformationException e) {
			return null;
		}
		sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf('.'));

		sourcePath += ".ham";
		String classpath = currentFile.getDir() + Utils.PSEP
				+ Workbench.getWorkbench().getProperty("classpath", "");
		String[] cpEntries = classpath.split(Utils.PSEP);
		for (int i = 0; i < cpEntries.length; i++) {
			File f = new File(cpEntries[i]);
			if (!f.isDirectory())
				continue;
			File file = new File(f, sourcePath);
			if (file.exists())
				return HamsterFile.getHamsterFile(file.getAbsolutePath());
		}
		return null;
	}

	private void removeTypeLocks() {
		for (int i = 0; i < lockedFiles.size(); i++) {
			HamsterFile file = (HamsterFile) lockedFiles.get(i);
			file.setLocked(false);
		}
		lockedFiles.removeAll(lockedFiles);
		lockedRefs.removeAll(lockedRefs);
	}

	private void lockType(ReferenceType ref) {
		HamsterFile file = getProgram(ref);
		if (file != null) {
			file.setLocked(true);
			lockedFiles.add(file);
		} else {
			if (ref.name().startsWith("java.")
					|| ref.name().startsWith("javax.")
					|| ref.name().startsWith("de.hamster.")
					|| ref.name().startsWith("sun."))
				return;
			lockedRefs.add(ref);
		}
	}

	private boolean removeEvent() throws InterruptedException,
			IncompatibleThreadStateException {
		EventSet set = eventQueue.remove();
		EventIterator eventIterator = set.eventIterator();
		boolean resume = false;
		while (eventIterator.hasNext()) {
			Event event = eventIterator.nextEvent();
			if (event instanceof VMStartEvent) {
				ClassPrepareRequest r = eventRequestManager
						.createClassPrepareRequest();
				r.enable();
				resume = true;
			} else if (event instanceof VMDisconnectEvent) {
				return true;
			} else if (event instanceof VMDeathEvent) {
				resume = true;
			} else if (event instanceof ClassPrepareEvent) {
				ClassPrepareEvent e = (ClassPrepareEvent) event;
				ReferenceType ref = e.referenceType();
				lockType(ref);
				if (ref.name().equals(className)) {
					MethodEntryRequest entryRequest = eventRequestManager
							.createMethodEntryRequest();
					entryRequest.addClassFilter(className);
					entryRequest.enable();
				}
				resume = true;
			} else if (event instanceof MethodEntryEvent) {
				MethodEntryEvent e = (MethodEntryEvent) event;
				Method m = e.method();
				if (m.isConstructor())
					resume = true;
				else {
					eventRequestManager.deleteEventRequest(event.request());
					suspendedThread = e.thread();
					stackFrames = e.thread().frames(0,
							e.thread().frameCount() - 1);
				}
			} else if (event instanceof StepEvent) {
				StepEvent e = (StepEvent) event;
				suspendedThread = e.thread();
				stackFrames = e.thread().frames(0, e.thread().frameCount() - 1);
			} else {
				resume = true;
			}
		}
		if (resume | !enabled) {
			set.resume();
		} else if (state == RUNNING) {
			setChanged();
			suspended = true;
			notifyObservers(ARG_STATE);
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if (state == RUNNING) {
				suspended = false;
				setChanged();
				notifyObservers(ARG_STATE);
				stepInto(currentFile);
			}
		} else {
			setState(PAUSED);
		}
		return false;
	}

	private void step(int stepDepth) {
		if (suspendedThread == null)
			return;
		if (stepRequest != null)
			eventRequestManager.deleteEventRequest(stepRequest);
		stepRequest = eventRequestManager.createStepRequest(suspendedThread,
				StepRequest.STEP_LINE, stepDepth);
		for (int i = 0; i < lockedRefs.size(); i++)
			stepRequest.addClassExclusionFilter(((ReferenceType) lockedRefs
					.get(i)).name());
		stepRequest.addClassExclusionFilter("java.*");
		stepRequest.addClassExclusionFilter("javax.*");
		stepRequest.addClassExclusionFilter("sun.*");
		stepRequest.addClassExclusionFilter("de.hamster.*");
		stepRequest.addCountFilter(1);
		stepRequest.enable();
		ThreadReference st = suspendedThread;
		suspendedThread = null;
		machine.resume();
	}

	public void setState(int state) {
		this.state = state;
		setChanged();
		if (java.awt.EventQueue.isDispatchThread()) {
			notifyObservers(ARG_STATE);
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						notifyObservers(ARG_STATE);
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled == enabled)
			return;
		this.enabled = enabled;
		if (state != NOT_RUNNING) {
			if (enabled) {
				runner.setDelay(0);
				stepInto(currentFile);
			} else {
				runner.setDelay(delay);
				if (state == RUNNING)
					machine.resume();
			}
		}
		setChanged();
		notifyObservers(ARG_ENABLE);
	}

	public void setDelay(int delay) {
		this.delay = delay;
		if (runner != null && !enabled)
			runner.setDelay(delay);
	}

	public int getDelay() {
		return this.delay;
	}

	public Thread getHamsterThread() {
		return hamsterThread;
	}
}