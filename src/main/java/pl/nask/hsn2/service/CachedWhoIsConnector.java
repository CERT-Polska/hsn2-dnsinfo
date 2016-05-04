/*
 * Copyright (c) NASK
 * 
 * This file is part of HoneySpider Network 2.1.
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedWhoIsConnector implements WhoIsConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachedWhoIsConnector.class);
	private static final int DEFAULT_SCAN_PERID = 100;
	
	private WhoIsConnector delegate;
	private final int cacheTime;
	private final int cacheLimit;
	
	private static Map<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();
	private int numberOfCalls = 0;
	private int scanInterval = DEFAULT_SCAN_PERID;

	public CachedWhoIsConnector(final WhoIsConnector delegate,
			final int cacheTime, final int cacheLimit) {

		if (delegate == null) {
			throw new IllegalArgumentException("Delegate must be privded.");
		}
		this.delegate = delegate;

		this.cacheLimit = cacheLimit;
		if (cacheLimit < this.scanInterval) {
			this.scanInterval = cacheLimit;
		}

		this.cacheTime = cacheTime;
	}

	public final void setScanInterval(int scanInterval) {
		this.scanInterval = scanInterval;
	}

	public final long getCacheTime() {
		return cacheTime;
	}

	public final long getCacheLimit() {
		return cacheLimit;
	}

	private void nocScan() {
		this.numberOfCalls++;
		if (this.cacheTime > 0 && this.numberOfCalls > this.scanInterval) {
			scanCache();
			this.numberOfCalls = 0;
		}
	}

	private CacheEntry getEntryFromCache(String domain) {
		CacheEntry entry = null;		
		LOGGER.debug("Checking entry in cache: {}", domain);
		if (cache.containsKey(domain)) {
			LOGGER.debug("Entry found.");
			entry = cache.get(domain);
			if (this.cacheTime > 0 && entry.isExpired(this.cacheTime)) {
				LOGGER.debug("Entry expired.");
				entry = null;
			}
		}
		return entry;
	}

	@Override
	public final String getWhoisData(String domain) {
		
		nocScan();
		
		CacheEntry entry = getEntryFromCache(domain);
		if (entry != null) {
			LOGGER.debug("Return cached entry.");
			return entry.getData();
		}
		
		String data = this.delegate.getWhoisData(domain);
		if (data == null) {
			LOGGER.warn("Empty whois data for domain {}", domain);
			return null;
		}
	
		if (this.cacheLimit > 0 && cache.size() >= this.cacheLimit) {
			scanCache(); // last chance to expire entries, and free up slots
		}
		
		if (this.cacheLimit == 0 || cache.size() < this.cacheLimit) {
			LOGGER.debug("Creating new entry.");
			entry = new CacheEntry(data);
			cache.put(domain, entry);
		} else if (this.cacheLimit > 0) {
			LOGGER.warn("Cache limit exceeded. Limit is {}.", this.cacheLimit);
		}
		
		return data;
	}

	private void scanCache() {
		LOGGER.debug("Scanning cache, cache size is {}", cache.size());
		long fried = 0;
		for (String key : cache.keySet()) {
			CacheEntry entry = cache.get(key);
			if (entry.isExpired(this.cacheTime)) {
				cache.remove(key);
				fried++;
			}
		}
		LOGGER.debug("Cache scan finished, firied {} entries", fried);
	}
	
	private static class CacheEntry {
		private final Long enterTime;
		private final String data;
		public CacheEntry(String data) {
			this.enterTime = System.currentTimeMillis();
			this.data = data;
		}
		public String getData() {
			return data;
		}
		public boolean isExpired(long secs) {
			long elapsed = TimeUnit.MILLISECONDS.toSeconds(
					System.currentTimeMillis() - this.enterTime);
			if (elapsed > secs) {
				return true;
			}
			return false;
		}
	}
}
