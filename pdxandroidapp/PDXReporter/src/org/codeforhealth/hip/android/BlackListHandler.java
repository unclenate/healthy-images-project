package org.codeforhealth.hip.android;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Roy Cham
 * 
 * Copyright 2010 City of Portland. All rights reserved.
 *
 */
public class BlackListHandler extends DefaultHandler {

	private StringBuffer accumulator;

	private boolean status = false;
	private boolean block = false;

	private static final String Y = "Y";
	private static final String YES = "YES";

	public BlackListHandler() {
		accumulator = new StringBuffer();
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	public boolean isBlackListed() {
		return block;
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		accumulator.setLength(0);

		/*
		 * Note, this assumes the xml only had 1 element of each, else the last
		 * one found will overwrite the previous.
		 * 
		 * ToDo: Add a tracker to which node you are currently in.
		 */

		if (localName.equalsIgnoreCase(Report.status))
			status = true;

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) {

		String temp = accumulator.toString().trim();

		if (status) {
			if (StringUtils.isNotBlank(temp)) {
				if ((temp.compareToIgnoreCase(BlackListHandler.Y) == 0)
						|| temp.compareToIgnoreCase(BlackListHandler.YES) == 0) {
					block = true;
				}

			}
			status = false;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		accumulator.append(ch, start, length);
	}

}
