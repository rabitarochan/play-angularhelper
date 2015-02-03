package com.github.rabitarochan.play2.angularhelper

import play.api.libs.json._
import scala.util.matching.Regex

sealed trait JsType {
  def parse(key: String, kvs: Seq[(String, String)]): JsValue
}

case class JsBooleanType(default: Boolean) extends JsType {

  def parse(key: String, kvs: Seq[(String, String)]): JsValue = {
    kvs.find(_._1 == key) match {
      case Some((_, v)) => JsBoolean(v.toBoolean)
      case None         => JsBoolean(default)
    }
  }
}

case class JsNumberType(default: Int) extends JsType {
  def parse(key: String, kvs: Seq[(String, String)]): JsValue = {
    kvs.find(_._1 == key) match {
      case Some((_, value)) => JsNumber(BigDecimal(value))
      case None             => JsNumber(default)
    }
  }
}

case class JsStringType(default: String) extends JsType {
  def parse(key: String, kvs: Seq[(String, String)]): JsValue = {
    kvs.find(_._1 == key) match {
      case Some((_, v)) => JsString(v)
      case None         => JsString(default)
    }
  }
}

case class JsObjectType(properties: (String, JsType)*) extends JsType {
  def parse(key: String, kvs: Seq[(String, String)]): JsValue = {
    kvs.filter(_._1.startsWith(key + ".")) match {
      case Nil => JsObject(properties.map(x => x._1 -> x._2.parse(x._1, Nil)))
      case xs => {
        JsObject(
          properties.map { property =>
            val (pkey, ptype) = property
            pkey -> ptype.parse(pkey, xs.map(x => (x._1.substring((key + ".").length), x._2)))
          }
        )
      }
    }
  }
}

case class JsArrayType(innerJsType: JsType) extends JsType {
  def parse(key: String, kvs: Seq[(String, String)]): JsValue = {
    kvs.filter(p => isMatch(key, p._1)) match {
      case Nil => JsArray(Nil)
      case xs  => {
        JsArray(
          xs.groupBy(t => getGroupKey(key, t._1))
            .toSeq
            .sortBy(_._1)
            .map(p =>
              innerJsType.parse("", p._2.map(t => removeKey(key, t._1) -> t._2)))
        )
      }
    }
  }

  private def isMatch(key: String, s: String): Boolean = {
    arrayRegex(key).findPrefixMatchOf(s).isDefined
  }

  private def getGroupKey(key: String, s: String): String = {
    arrayRegex(key).findPrefixOf(s).get
  }

  private def removeKey(key: String, s: String): String = {
    arrayRegex(key).replaceFirstIn(s, "")
  }

  private def arrayRegex(key: String): Regex = s"${key}\\[[0-9]+\\]".r
}
