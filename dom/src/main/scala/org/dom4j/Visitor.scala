package org.dom4j

trait Visitor {
  def visit(node: Document)              : Unit
  def visit(node: Element)               : Unit
  def visit(node: Attribute)             : Unit
  def visit(node: CDATA)                 : Unit
  def visit(node: Comment)               : Unit
  def visit(node: Entity)                : Unit
  def visit(node: Namespace)             : Unit
  def visit(node: ProcessingInstruction) : Unit
  def visit(node: Text)                  : Unit
}
