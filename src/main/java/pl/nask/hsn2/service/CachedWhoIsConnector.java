package pl.nask.hsn2.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CachedWhoIsConnector implements WhoIsConnector {

	private static final Logger LOGGER = LoggerFactory.getLogger(CachedWhoIsConnector.class);
	
	private WhoIsConnector delegate;
	private final int cacheTime;
	private final int cacheLimit;
	
	private Map<String, CacheEntry> cache = new ConcurrentHashMap<String, CacheEntry>();
	private int numberOfCalls = 0;
	private int scanInterval = 100;

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

	@Override
	public final String getWhoisData(String domain) {
		
		this.numberOfCalls++;
		if (this.cacheTime > 0 && this.numberOfCalls > this.scanInterval) {
			scanCache();
			this.numberOfCalls = 0;
		}
		
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
		
		String data = null;
		if (entry == null) {
			data = this.delegate.getWhoisData(domain);
			if (data != null) {
				
				if (cache.size() >= this.cacheLimit) {
					scanCache(); // last chance to expire entries, and free up slots
				}
				
				if (cache.size() < this.cacheLimit) {
					LOGGER.debug("Creating new entry.");
					entry = new CacheEntry(data);
					cache.put(domain, entry);
				} else {
					LOGGER.warn("Cache limit exceeded. Limit is {}.", this.cacheLimit);
				}
			} else {
				LOGGER.warn("Empty whois data for domain {}", domain);
			}
		} else {
			LOGGER.debug("Return cached entry.");
			data = entry.getData();
		}
		
		return data;
	}

	private void scanCache() {
		LOGGER.debug("Scanning cache, cache size is {}", cache.size());
		long fried = 0;
		for (String key : this.cache.keySet()) {
			CacheEntry entry = this.cache.get(key);
			if (entry.isExpired(this.cacheTime)) {
				cache.remove(key);
				fried++;
			}
		}
		LOGGER.debug("Cache scan finished, firied {} entries", fried);
	}
	
	private class CacheEntry {
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
			long elapsed = (System.currentTimeMillis() - this.enterTime)/1000;
			if (elapsed > secs) {
				return true;
			}
			return false;
		}
	}
}
