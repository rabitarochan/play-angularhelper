package com.github.rabitarochan.play2.angularhelper

object InitHelper {

  implicit class FormExtension(val form: play.api.data.Form[_]) extends AnyVal {

    def ngInit(tuple: (String, JsType)): String = {
      val (key, jsType) = tuple
      jsType.parse(key, form.data.toSeq).toString()
    }

  }

  def jsBoolean: JsType = jsBoolean(false)

  def jsBoolean(default: Boolean): JsType = JsBooleanType(default)

  def jsNumber: JsType = jsNumber(0)

  def jsNumber(default: Int): JsType = JsNumberType(default)

  def jsString: JsType = jsString("")

  def jsString(defaultValue: String): JsType = JsStringType(defaultValue)

  def jsObject(properties: (String, JsType)*): JsType = JsObjectType(properties: _*)

  def jsArray(jsType: JsType): JsType = JsArrayType(jsType)

}
