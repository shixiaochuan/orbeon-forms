/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * <code>Branch</code> interface defines the common behaviour for Nodes which
 * can contain child nodes (content) such as XML elements and documents. This
 * interface allows both elements and documents to be treated in a polymorphic
 * manner when changing or navigating child nodes (content).
 * </p>
 *
 * @author James Strachan
 */
public interface Branch extends Node {
    /**
     * Returns the <code>Node</code> at the specified index position.
     *
     * @param index
     *            the index of the node to return.
     *
     * @return the <code>Node</code> at the specified position.
     *
     * @throws IndexOutOfBoundsException
     *             if the index is out of range (index &lt; 0 || index &gt;=
     *             {@link Branch#nodeCount()}).
     */
    Node node(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the index of the given node if it is a child node of this branch
     * or -1 if the given node is not a child node.
     *
     * @param node
     *            the content child node to find.
     *
     * @return the index of the given node starting at 0 or -1 if the node is
     *         not a child node of this branch
     */
    int indexOf(Node node);

    /**
     * Returns the number of <code>Node</code> instances that this branch
     * contains.
     *
     * @return the number of nodes this branch contains
     */
    int nodeCount();

    /**
     * <p>
     * Returns the content nodes of this branch as a backed {@link List}so that
     * the content of this branch may be modified directly using the
     * {@link List}interface. The <code>List</code> is backed by the
     * <code>Branch</code> so that changes to the list are reflected in the
     * branch and vice versa.
     * </p>
     *
     * @return the nodes that this branch contains as a <code>List</code>
     */
    List<Node> content();

    /**
     * Returns an iterator through the content nodes of this branch
     *
     * @return an iterator through the content nodes of this branch
     */
    Iterator nodeIterator();

    /**
     * Appends the content of the given branch to this branch instance. This
     * method behaves like the {@link
     * java.util.Collection#addAll(java.util.Collection)} method.
     *
     * @param branch
     *            is the branch whose content will be added to me.
     */
    void appendContent(Branch branch);

    /**
     * Clears the content for this branch, removing any <code>Node</code>
     * instances this branch may contain.
     */
    void clearContent();

    /**
     * Adds a new <code>Element</code> node with the given name to this branch
     * and returns a reference to the new node.
     *
     * @param name
     *            is the name for the <code>Element</code> node.
     *
     * @return the newly added <code>Element</code> node.
     */
    Element addElement(String name);

    /**
     * Adds a new <code>Element</code> node with the given {@link QName}to
     * this branch and returns a reference to the new node.
     *
     * @param qname
     *            is the qualified name for the <code>Element</code> node.
     *
     * @return the newly added <code>Element</code> node.
     */
    Element addElement(QName qname);

    /**
     * Adds a new <code>Element</code> node with the given qualified name and
     * namespace URI to this branch and returns a reference to the new node.
     *
     * @param qualifiedName
     *            is the fully qualified name of the Element
     * @param namespaceURI
     *            is the URI of the namespace to use
     *
     * @return the newly added <code>Element</code> node.
     */
    Element addElement(String qualifiedName, String namespaceURI);

    /**
     * Adds the given <code>Node</code> or throws {@link IllegalAddException}
     * if the given node is not of a valid type. This is a polymorphic method
     * which will call the typesafe method for the node type such as
     * add(Element) or add(Comment).
     *
     * @param node
     *            is the given node to add
     */
    void add(Node node);

    /**
     * Adds the given <code>Comment</code> to this branch. If the given node
     * already has a parent defined then an <code>IllegalAddException</code>
     * will be thrown.
     *
     * @param comment
     *            is the comment to be added
     */
    void add(Comment comment);

    /**
     * Adds the given <code>Element</code> to this branch. If the given node
     * already has a parent defined then an <code>IllegalAddException</code>
     * will be thrown.
     *
     * @param element
     *            is the element to be added
     */
    void add(Element element);

    /**
     * Adds the given <code>ProcessingInstruction</code> to this branch. If
     * the given node already has a parent defined then an
     * <code>IllegalAddException</code> will be thrown.
     *
     * @param pi
     *            is the processing instruction to be added
     */
    void add(ProcessingInstruction pi);

    /**
     * Removes the given <code>Node</code> if the node is an immediate child
     * of this branch. If the given node is not an immediate child of this
     * branch then the {@link Node#detach()}method should be used instead. This
     * is a polymorphic method which will call the typesafe method for the node
     * type such as remove(Element) or remove(Comment).
     *
     * @param node
     *            is the given node to be removed
     *
     * @return true if the node was removed
     */
    boolean remove(Node node);

    /**
     * Removes the given <code>Comment</code> if the node is an immediate
     * child of this branch. If the given node is not an immediate child of this
     * branch then the {@link Node#detach()}method should be used instead.
     *
     * @param comment
     *            is the comment to be removed
     *
     * @return true if the comment was removed
     */
    boolean remove(Comment comment);

    /**
     * Removes the given <code>Element</code> if the node is an immediate
     * child of this branch. If the given node is not an immediate child of this
     * branch then the {@link Node#detach()}method should be used instead.
     *
     * @param element
     *            is the element to be removed
     *
     * @return true if the element was removed
     */
    boolean remove(Element element);

    /**
     * Removes the given <code>ProcessingInstruction</code> if the node is an
     * immediate child of this branch. If the given node is not an immediate
     * child of this branch then the {@link Node#detach()}method should be used
     * instead.
     *
     * @param pi
     *            is the processing instruction to be removed
     *
     * @return true if the processing instruction was removed
     */
    boolean remove(ProcessingInstruction pi);

    /**
     * Puts all <code>Text</code> nodes in the full depth of the sub-tree
     * underneath this <code>Node</code>, including attribute nodes, into a
     * "normal" form where only structure (e.g., elements, comments, processing
     * instructions, CDATA sections, and entity references) separates
     * <code>Text</code> nodes, i.e., there are neither adjacent
     * <code>Text</code> nodes nor empty <code>Text</code> nodes. This can
     * be used to ensure that the DOM view of a document is the same as if it
     * were saved and re-loaded, and is useful when operations (such as XPointer
     * lookups) that depend on a particular document tree structure are to be
     * used.In cases where the document contains <code>CDATASections</code>,
     * the normalize operation alone may not be sufficient, since XPointers do
     * not differentiate between <code>Text</code> nodes and
     * <code>CDATASection</code> nodes.
     *
     * @since DOM Level 2
     */
    void normalize();
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
