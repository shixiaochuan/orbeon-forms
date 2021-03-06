/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.ElementHandler;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.URL;

/**
 * <p>
 * <code>SAXReader</code> creates a DOM4J tree from SAX parsing events.
 * </p>
 *
 * <p>
 * The actual SAX parser that is used by this class is configurable so you can
 * use your favourite SAX parser if you wish. DOM4J comes configured with its
 * own SAX parser so you do not need to worry about configuring the SAX parser.
 * </p>
 *
 * <p>
 * To explicitly configure the SAX parser that is used via Java code you can use
 * a constructor or use the {@link #setXMLReader(XMLReader)}or {@link
 * #setXMLReaderClassName(String)} methods.
 * </p>
 *
 * <p>
 * If the parser is not specified explicitly then the standard SAX policy of
 * using the <code>org.xml.sax.driver</code> system property is used to
 * determine the implementation class of {@link XMLReader}.
 * </p>
 *
 * <p>
 * If the <code>org.xml.sax.driver</code> system property is not defined then
 * JAXP is used via reflection (so that DOM4J is not explicitly dependent on the
 * JAXP classes) to load the JAXP configured SAXParser. If there is any error
 * creating a JAXP SAXParser an informational message is output and then the
 * default (Aelfred) SAX parser is used instead.
 * </p>
 *
 * <p>
 * If you are trying to use JAXP to explicitly set your SAX parser and are
 * experiencing problems, you can turn on verbose error reporting by defining
 * the system property <code>org.dom4j.verbose</code> to be "true" which will
 * output a more detailed description of why JAXP could not find a SAX parser
 * </p>
 *
 * <p>
 * For more information on JAXP please go to <a
 * href="http://java.sun.com/xml/">Sun's Java &amp; XML site </a>
 * </p>
 *
 * @author James Strachan
 */
public class SAXReader {
    private static final String SAX_STRING_INTERNING =
            "http://xml.org/sax/features/string-interning";
    private static final String SAX_NAMESPACE_PREFIXES =
            "http://xml.org/sax/features/namespace-prefixes";
    private static final String SAX_NAMESPACES =
            "http://xml.org/sax/features/namespaces";
    private static final String SAX_DECL_HANDLER =
            "http://xml.org/sax/properties/declaration-handler";
    private static final String SAX_LEXICAL_HANDLER =
            "http://xml.org/sax/properties/lexical-handler";
    private static final String SAX_LEXICALHANDLER =
            "http://xml.org/sax/handlers/LexicalHandler";

    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory;

    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;

    /** Whether validation should occur */
    private boolean validating;

    /** DispatchHandler to call when each <code>Element</code> is encountered */
    private DispatchHandler dispatchHandler;

    /** ErrorHandler class to use */
    private ErrorHandler errorHandler;

    /** The entity resolver */
    private EntityResolver entityResolver;

    /** Should element & attribute names and namespace URIs be interned? */
    private boolean stringInternEnabled = true;

    /** Should internal DTD declarations be expanded into a List in the DTD */
    private boolean includeInternalDTDDeclarations = false;

    /** Should external DTD declarations be expanded into a List in the DTD */
    private boolean includeExternalDTDDeclarations = false;

    /** Whether adjacent text nodes should be merged */
    private boolean mergeAdjacentText = false;

    /** Holds value of property stripWhitespaceText. */
    private boolean stripWhitespaceText = false;

    /** Should we ignore comments */
    private boolean ignoreComments = false;

    /** Encoding of InputSource - null means system default encoding */
    private String encoding = null;

    public SAXReader(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    public SAXReader() {
    }

    /**
     * <p>
     * Reads a Document from the given <code>URL</code> using SAX
     * </p>
     *
     * @param url
     *            <code>URL</code> to read from.
     *
     * @return the newly created Document instance
     *
     * @throws DocumentException
     *             if an error occurs during parsing.
     */
    public Document read(URL url) throws DocumentException {
        String systemID = url.toExternalForm();

        InputSource source = new InputSource(systemID);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }

        return read(source);
    }

    /**
     * <p>
     * Reads a Document from the given stream using SAX
     * </p>
     *
     * @param in
     *            <code>InputStream</code> to read from.
     *
     * @return the newly created Document instance
     *
     * @throws DocumentException
     *             if an error occurs during parsing.
     */
    public Document read(InputStream in) throws DocumentException {
        InputSource source = new InputSource(in);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }

        return read(source);
    }

    /**
     * <p>
     * Reads a Document from the given <code>Reader</code> using SAX
     * </p>
     *
     * @param reader
     *            is the reader for the input
     *
     * @return the newly created Document instance
     *
     * @throws DocumentException
     *             if an error occurs during parsing.
     */
    public Document read(Reader reader) throws DocumentException {
        InputSource source = new InputSource(reader);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }

        return read(source);
    }

    /**
     * <p>
     * Reads a Document from the given stream using SAX
     * </p>
     *
     * @param in
     *            <code>InputStream</code> to read from.
     * @param systemId
     *            is the URI for the input
     *
     * @return the newly created Document instance
     *
     * @throws DocumentException
     *             if an error occurs during parsing.
     */
    public Document read(InputStream in, String systemId)
            throws DocumentException {
        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }

        return read(source);
    }

    /**
     * <p>
     * Reads a Document from the given <code>Reader</code> using SAX
     * </p>
     *
     * @param reader
     *            is the reader for the input
     * @param systemId
     *            is the URI for the input
     *
     * @return the newly created Document instance
     *
     * @throws DocumentException
     *             if an error occurs during parsing.
     */
    public Document read(Reader reader, String systemId)
            throws DocumentException {
        InputSource source = new InputSource(reader);
        source.setSystemId(systemId);
        if (this.encoding != null) {
            source.setEncoding(this.encoding);
        }

        return read(source);
    }

    /**
     * <p>
     * Reads a Document from the given <code>InputSource</code> using SAX
     * </p>
     *
     * @param in
     *            <code>InputSource</code> to read from.
     *
     * @return the newly created Document instance
     *
     * @throws DocumentException
     *             if an error occurs during parsing.
     */
    public Document read(InputSource in) throws DocumentException {
        try {
            XMLReader reader = getXMLReader();

            EntityResolver thatEntityResolver = this.entityResolver;

            if (thatEntityResolver == null) {
                thatEntityResolver = createDefaultEntityResolver(in
                        .getSystemId());
                this.entityResolver = thatEntityResolver;
            }

            reader.setEntityResolver(thatEntityResolver);

            SAXContentHandler contentHandler = createContentHandler(reader);
            contentHandler.setEntityResolver(thatEntityResolver);
            contentHandler.setInputSource(in);

            contentHandler.setMergeAdjacentText(isMergeAdjacentText());
            contentHandler.setStripWhitespaceText(isStripWhitespaceText());
            contentHandler.setIgnoreComments(isIgnoreComments());
            reader.setContentHandler(contentHandler);

            configureReader(reader, contentHandler);

            reader.parse(in);

            return contentHandler.getDocument();
        } catch (Exception e) {
            if (e instanceof SAXParseException) {
                // e.printStackTrace();
                SAXParseException parseException = (SAXParseException) e;
                String systemId = parseException.getSystemId();

                if (systemId == null) {
                    systemId = "";
                }

                String message = "Error on line "
                        + parseException.getLineNumber() + " of document "
                        + systemId + " : " + parseException.getMessage();

                throw new DocumentException(message, e);
            } else {
                throw new DocumentException(e.getMessage(), e);
            }
        }
    }

    // Properties
    // -------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return the validation mode, true if validating will be done otherwise
     *         false.
     */
    public boolean isValidating() {
        return validating;
    }

    /**
     * DOCUMENT ME!
     *
     * @return whether internal DTD declarations should be expanded into the
     *         DocumentType object or not.
     */
    public boolean isIncludeInternalDTDDeclarations() {
        return includeInternalDTDDeclarations;
    }

    /**
     * DOCUMENT ME!
     *
     * @return whether external DTD declarations should be expanded into the
     *         DocumentType object or not.
     */
    public boolean isIncludeExternalDTDDeclarations() {
        return includeExternalDTDDeclarations;
    }

    /**
     * Sets whether String interning is enabled or disabled for element &
     * attribute names and namespace URIs. This proprety is enabled by default.
     *
     * @return DOCUMENT ME!
     */
    public boolean isStringInternEnabled() {
        return stringInternEnabled;
    }

    /**
     * Returns whether adjacent text nodes should be merged together.
     *
     * @return Value of property mergeAdjacentText.
     */
    public boolean isMergeAdjacentText() {
        return mergeAdjacentText;
    }

    /**
     * Sets whether whitespace between element start and end tags should be
     * ignored
     *
     * @return Value of property stripWhitespaceText.
     */
    public boolean isStripWhitespaceText() {
        return stripWhitespaceText;
    }

    /**
     * Returns whether we should ignore comments or not.
     *
     * @return boolean
     */
    public boolean isIgnoreComments() {
        return ignoreComments;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the <code>DocumentFactory</code> used to create document
     *         objects
     */
    public DocumentFactory getDocumentFactory() {
        if (factory == null) {
            factory = DocumentFactory.getInstance();
        }

        return factory;
    }

    /**
     * <p>
     * This sets the <code>DocumentFactory</code> used to create new
     * documents. This method allows the building of custom DOM4J tree objects
     * to be implemented easily using a custom derivation of
     * {@link DocumentFactory}
     * </p>
     *
     * @param documentFactory
     *            <code>DocumentFactory</code> used to create DOM4J objects
     */
    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.factory = documentFactory;
    }

    /**
     * DOCUMENT ME!
     *
     * @return the <code>XMLReader</code> used to parse SAX events
     *
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = createXMLReader();
        }

        return xmlReader;
    }

    /**
     * Sets the <code>XMLReader</code> used to parse SAX events
     *
     * @param reader
     *            is the <code>XMLReader</code> to parse SAX events
     */
    public void setXMLReader(XMLReader reader) {
        this.xmlReader = reader;
    }

    /**
     * Returns encoding used for InputSource (null means system default
     * encoding)
     *
     * @return encoding used for InputSource
     *
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets encoding used for InputSource (null means system default encoding)
     *
     * @param encoding
     *            is encoding used for InputSource
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Factory Method to allow alternate methods of creating and configuring
     * XMLReader objects
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException
     *             DOCUMENT ME!
     */
    protected XMLReader createXMLReader() throws SAXException {
        return SAXHelper.createXMLReader(isValidating());
    }

    /**
     * Configures the XMLReader before use
     *
     * @param reader
     *            DOCUMENT ME!
     * @param handler
     *            DOCUMENT ME!
     *
     * @throws DocumentException
     *             DOCUMENT ME!
     */
    protected void configureReader(XMLReader reader, DefaultHandler handler)
            throws DocumentException {
        // configure lexical handling
        SAXHelper.setParserProperty(reader, SAX_LEXICALHANDLER, handler);

        // try alternate property just in case
        SAXHelper.setParserProperty(reader, SAX_LEXICAL_HANDLER, handler);

        // register the DeclHandler
        if (includeInternalDTDDeclarations || includeExternalDTDDeclarations) {
            SAXHelper.setParserProperty(reader, SAX_DECL_HANDLER, handler);
        }

        // configure namespace support
        SAXHelper.setParserFeature(reader, SAX_NAMESPACES, true);

        SAXHelper.setParserFeature(reader, SAX_NAMESPACE_PREFIXES, false);

        // string interning
        SAXHelper.setParserFeature(reader, SAX_STRING_INTERNING,
                isStringInternEnabled());

        // external entites
        /*
         * SAXHelper.setParserFeature( reader,
         * "http://xml.org/sax/properties/external-general-entities",
         * includeExternalGeneralEntities ); SAXHelper.setParserFeature( reader,
         * "http://xml.org/sax/properties/external-parameter-entities",
         * includeExternalParameterEntities );
         */
        // use Locator2 if possible
        SAXHelper.setParserFeature(reader,
                "http://xml.org/sax/features/use-locator2", true);

        try {
            // configure validation support
            reader.setFeature("http://xml.org/sax/features/validation",
                    isValidating());

            if (errorHandler != null) {
                reader.setErrorHandler(errorHandler);
            } else {
                reader.setErrorHandler(handler);
            }
        } catch (Exception e) {
            if (isValidating()) {
                throw new DocumentException("Validation not supported for"
                        + " XMLReader: " + reader, e);
            }
        }
    }

    /**
     * Factory Method to allow user derived SAXContentHandler objects to be used
     *
     * @param reader
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected SAXContentHandler createContentHandler(XMLReader reader) {
        return new SAXContentHandler(getDocumentFactory(), dispatchHandler);
    }

    protected EntityResolver createDefaultEntityResolver(String systemId) {
        String prefix = null;

        if ((systemId != null) && (systemId.length() > 0)) {
            int idx = systemId.lastIndexOf('/');

            if (idx > 0) {
                prefix = systemId.substring(0, idx + 1);
            }
        }

        return new SAXEntityResolver(prefix);
    }

    protected static class SAXEntityResolver implements EntityResolver,
            Serializable {
        protected String uriPrefix;

        public SAXEntityResolver(String uriPrefix) {
            this.uriPrefix = uriPrefix;
        }

        public InputSource resolveEntity(String publicId, String systemId) {
            // try create a relative URI reader...
            if ((systemId != null) && (systemId.length() > 0)) {
                if ((uriPrefix != null) && (systemId.indexOf(':') <= 0)) {
                    systemId = uriPrefix + systemId;
                }
            }

            return new InputSource(systemId);
        }
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
