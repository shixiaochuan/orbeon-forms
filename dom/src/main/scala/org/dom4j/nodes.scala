package org.dom4j

trait CharacterData extends Node {
  def appendText(text: String): Unit
}

trait CDATA   extends CharacterData
trait Comment extends CharacterData
trait Text    extends CharacterData

trait Entity  extends Node
