package org.dom4j.tree

import org.dom4j.{Node, Text, Visitor}

class ConcreteText(var text: String) extends WithCharacterData with Text {

  override def getNodeType = Node.TEXT_NODE

  override def getText               = text
  override def setText(text: String) = { this.text = text }

  def accept(visitor: Visitor)       = visitor.visit(this)

  override def toString: String = {
    super.toString + " [Text: \"" + getText + "\"]"
  }
}
