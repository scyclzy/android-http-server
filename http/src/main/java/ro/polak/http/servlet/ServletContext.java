/**************************************************
 * Android Web Server
 * Based on JavaLittleWebServer (2008)
 * <p/>
 * Copyright (c) Piotr Polak 2016-2016
 **************************************************/

package ro.polak.http.servlet;

import java.util.Enumeration;

/**
 * Servlet context.
 *
 * @author Piotr Polak piotr [at] polak [dot] ro
 * @since 201610
 */
public interface ServletContext {

    /**
     * Sets context attribute.
     *
     * @param name
     * @param value
     * @throws IllegalStateException
     */
    void setAttribute(String name, Object value);

    /**
     * Gets context attribute of the specified name.
     *
     * @param name Attribute name
     * @return
     * @throws IllegalStateException
     */
    Object getAttribute(String name);

    /**
     * Returns enumeration representing attribute names.
     *
     * @return
     * @throws IllegalStateException
     */
    Enumeration getAttributeNames();

    /**
     * Returns the MIME type of the specified file, or null if the MIME type is not known.
     *
     * @param file
     * @return
     */
    String getMimeType(String file);
}
