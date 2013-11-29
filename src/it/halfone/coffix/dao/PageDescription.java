package it.halfone.coffix.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * PageDescription - 22/nov/2013
 *
 * @author Andrea La Rosa
 */
public class PageDescription {

	private Collection<String> persistentCssFiles = new ArrayList<>();
	
	private Collection<String> cssFiles = new ArrayList<>();
	private Collection<String> jsFiles = new ArrayList<>();
	private Map<String, Object> data = new HashMap<>();
	
	
	public Collection<String> getPersistentCssFiles() {
		return persistentCssFiles;
	}
	public void setPersistentCssFiles(Collection<String> persistentCssFiles) {
		this.persistentCssFiles = persistentCssFiles;
	}
	public Collection<String> getCssFiles() {
		return cssFiles;
	}
	public void setCssFiles(Collection<String> cssFiles) {
		this.cssFiles = cssFiles;
	}
	public Collection<String> getJsFiles() {
		return jsFiles;
	}
	public void setJsFiles(Collection<String> jsFiles) {
		this.jsFiles = jsFiles;
	}
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}
