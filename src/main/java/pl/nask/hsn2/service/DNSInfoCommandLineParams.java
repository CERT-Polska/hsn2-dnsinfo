/*
 * Copyright (c) NASK, NCSC
 * 
 * This file is part of HoneySpider Network 2.0.
 * 
 * This is a free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.nask.hsn2.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.nask.hsn2.CommandLineParams;

public class DNSInfoCommandLineParams extends CommandLineParams {
	
	private static final OptionNameWrapper ZONES_PATH = new OptionNameWrapper("zp", "zonesPath");
	private static final OptionNameWrapper WHOSI_SERVERS_PATH = new OptionNameWrapper("wsp", "whoisServersPath");
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DNSInfoCommandLineParams.class);

	@Override
	public final void initOptions() {
		super.initOptions();
		addOption(ZONES_PATH, "path", "path to zones file");
		addOption(WHOSI_SERVERS_PATH, "path", "path to whois servers list file");
	}
	
	@Override
	protected final void initDefaults() {
		super.initDefaults();
		setDefaultServiceNameAndQueueName("dns-info");
		setDefaultMaxThreads(1);
	}
	
	public final String getZonesPath() {
		return getOptionValue(ZONES_PATH);
	}
	
	public final String getWhoisServersPath() {
		return getOptionValue(WHOSI_SERVERS_PATH);
	}

	@Override
	protected final void validate(){
		super.validate();
		String msg = "";
		if (!new File(getZonesPath()).exists()){
			msg += "ZonesPath not exists!\n";
			LOGGER.error("ZonesPath does not exist! Path used: {}", getZonesPath());
		}
		if (!new File(getWhoisServersPath()).exists()){
			msg += "WhoisServersPath not exists!\n";
			LOGGER.error("WhoisServersPath does not exist! Path used: {}", getWhoisServersPath());
		}
		if (!"".equals(msg)){
			throw new IllegalStateException(msg);
		}
	}
}
