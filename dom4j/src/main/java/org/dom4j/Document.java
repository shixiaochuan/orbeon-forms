/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import org.xml.sax.EntityResolver;

import java.util.Map;

/**
 * <p>
 * <code>Document</code> defines an XML Document.
 * </p>
 *
 * @author James Strachan
 */
public interface Document extends Branch {
    /**
     * Returns the root {@link Element}for this document.
     *
     * @return the root element for this document
     */
    Element getRootElement();

    /**
     * Sets the root element for this document
     *
     * @param rootElement
     *            the new root element for this document
     */
    void setRootElement(Element rootElement);

    /**
     * Adds a new <code>Comment</code> node with the given text to this
     * branch.
     *
     * @param comment
     *            is the text for the <code>Comment</code> node.
     *
     * @return this <code>Document</code> instance.
     */
    Document addComment(String comment);

    /**
     * Adds a processing instruction for the given target
     *
     * @param target
     *            is the target of the processing instruction
     * @param text
     *            is the textual data (key/value pairs) of the processing
     *            instruction
     *
     * @return this <code>Document</code> instance.
     */
    Document addProcessingInstruction(String target, String text);

    /**
     * Sets the EntityResolver used to find resolve URIs such as for DTDs, or
     * XML Schema documents
     *
     * @param entityResolver
     *            DOCUMENT ME!
     */
    void setEntityResolver(EntityResolver entityResolver);
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
