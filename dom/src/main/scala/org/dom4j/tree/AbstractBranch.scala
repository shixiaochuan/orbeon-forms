package org.dom4j.tree

import java.{lang ⇒ jl, util ⇒ ju}

import org.dom4j.Node._
import org.dom4j._

abstract class AbstractBranch extends AbstractNode with Branch {

  protected def internalContent: ju.List[Node]

  override def hasContent: Boolean = nodeCount > 0

  override def getText: String = {
    val list = internalContent
    if (list ne null) {
      val size = list.size
      if (size >= 1) {
        val first = list.get(0)
        val firstText = getContentAsText(first)
        if (size == 1) {
          return firstText
        } else {
          val buffer = new jl.StringBuilder(firstText)
          for (i ← 1 until size) {
            val node = list.get(i)
            buffer.append(getContentAsText(node))
          }
          return buffer.toString
        }
      }
    }
    ""
  }

  /**
   * @return the text value of the given content object as text which returns
   *         the text value of CDATA, Entity or Text nodes
   */
  private def getContentAsText(content: Node): String = {
    // ORBEON: match on trait
    content match {
      case node: Node ⇒
        node.getNodeType match {
          case CDATA_SECTION_NODE | ENTITY_REFERENCE_NODE | TEXT_NODE ⇒ return node.getText
          case _ ⇒
        }
      case _ ⇒
    }
    ""
  }

  /**
   * @return the XPath defined string-value of the given content object
   */
  protected def getContentAsStringValue(content: AnyRef): String = {
    content match {
      case node: Node ⇒
        // ORBEON TODO: match on trait
        node.getNodeType match {
          case CDATA_SECTION_NODE | ENTITY_REFERENCE_NODE | TEXT_NODE | ELEMENT_NODE ⇒ node.getStringValue
          case _ ⇒ ""
        }
      case s: String ⇒
        s
      case _ ⇒
        ""
    }
  }

  def getTextTrim: String = {
    val text = getText
    val textContent = new jl.StringBuilder
    val tokenizer = new ju.StringTokenizer(text)
    while (tokenizer.hasMoreTokens) {
      val str = tokenizer.nextToken()
      textContent.append(str)
      if (tokenizer.hasMoreTokens) {
        textContent.append(" ")
      }
    }
    textContent.toString
  }

  def addElement(name: String): Element = {
    val node = DocumentFactory.createElement(name)
    add(node)
    node
  }

  def addElement(qualifiedName: String, namespaceURI: String): Element = {
    val node = DocumentFactory.createElement(qualifiedName, namespaceURI)
    add(node)
    node
  }

  def addElement(qname: QName): Element = {
    val node = DocumentFactory.createElement(qname)
    add(node)
    node
  }

  def addElement(name: String, prefix: String, uri: String): Element = {
    val namespace = Namespace.get(prefix, uri)
    val qName = DocumentFactory.createQName(name, namespace)
    addElement(qName)
  }

  // ORBEON TODO: match on trait
  def add(node: Node) = node.getNodeType match {
    case ELEMENT_NODE                ⇒ add(node.asInstanceOf[Element])
    case COMMENT_NODE                ⇒ add(node.asInstanceOf[Comment])
    case PROCESSING_INSTRUCTION_NODE ⇒ add(node.asInstanceOf[ProcessingInstruction])
    case _                           ⇒ invalidNodeTypeException(node)
  }

  // ORBEON TODO: match on trait
  def remove(node: Node): Boolean = node.getNodeType match {
    case ELEMENT_NODE                ⇒ remove(node.asInstanceOf[Element])
    case COMMENT_NODE                ⇒ remove(node.asInstanceOf[Comment])
    case PROCESSING_INSTRUCTION_NODE ⇒ remove(node.asInstanceOf[ProcessingInstruction])
    case _                           ⇒ invalidNodeTypeException(node); false
  }

  def add(comment: Comment)             : Unit    = addNode(comment)
  def add(element: Element)             : Unit
  def add(pi: ProcessingInstruction)    : Unit    = addNode(pi)

  def remove(comment: Comment)          : Boolean = removeNode(comment)
  def remove(element: Element)          : Boolean
  def remove(pi: ProcessingInstruction) : Boolean = removeNode(pi)

  def appendContent(branch: Branch): Unit = {
    for (i ← 0 until branch.nodeCount) {
      val node = branch.node(i)
      add(node.clone().asInstanceOf[Node])
    }
  }

  def node(index: Int): Node =
    internalContent.get(index) match {
      case node: Node ⇒ node
      case _          ⇒ null
    }

  def nodeCount: Int = internalContent.size
  def nodeIterator: ju.Iterator[Node] = internalContent.iterator

  protected def addNode(node: Node): Unit
  protected def addNode(index: Int, node: Node): Unit
  protected def removeNode(node: Node): Boolean

  /**
   * Called when a new child node has been added to me to allow any parent
   * relationships to be created or  events to be fired.
   */
  protected[dom4j] def childAdded(node: Node): Unit

  /**
   * Called when a child node has been removed to allow any parent
   * relationships to be deleted or events to be fired.
   */
  protected[dom4j] def childRemoved(node: Node): Unit

  private def invalidNodeTypeException(node: Node) =
    throw new IllegalAddException("Invalid node type. Cannot add node: " + node + " to this branch: " + this)
}
