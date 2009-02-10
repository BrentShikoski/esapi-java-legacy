/*
 * OWASP ESAPI WAF
 *
 * JAK 1.0
 * Copyright (c) 2004-2005 Ivan Ristic <ivanr@webkreator.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package org.owasp.esapi.filters.waf.jak;

import java.io.IOException;

/**
 * This interface will be implemented by classes that
 * wish to provide configuration directives for
 * processing.
 *
 */
public interface DirectiveProvider {
	public DirectiveProvider construct(String filename);
	public void open() throws IOException;
	public void close();

	public boolean hasNext() throws IOException;
	public Directive getNext() throws IOException;

	public int getLineNumber();
	public String getSource();
}
