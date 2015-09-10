package pl.nask.hsn2.service;

import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;

import pl.nask.hsn2.CommandLineParams;
import pl.nask.hsn2.ServiceMain;
import pl.nask.hsn2.task.TaskFactory;

public class DNSInfoService extends ServiceMain {

	public static void main(final String[] args) throws DaemonInitException, InterruptedException {
		ServiceMain dis = new DNSInfoService();
		dis.init(new DaemonContext() {
			public DaemonController getController() {
				return null;
			}
			public String[] getArguments() {
				return args;
			}
		});
		dis.start();
		dis.getServiceRunner().join();
	}

	@Override
	protected final void prepareService() {
	}
	
	@Override
	protected final Class<? extends TaskFactory> initializeTaskFactory() {
		DNSInfoServiceTaskFactory.prepereForAllThreads((DNSInfoCommandLineParams)getCommandLineParams());
		return DNSInfoServiceTaskFactory.class;
	}
	
	@Override
	protected final CommandLineParams newCommandLineParams() {
		return new DNSInfoCommandLineParams();
	}

}
