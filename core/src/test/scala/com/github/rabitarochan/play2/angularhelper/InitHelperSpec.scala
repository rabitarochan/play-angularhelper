package com.github.rabitarochan.play2.angularhelper

import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import org.scalatest._

class InitHelperSpec extends FunSpec with Matchers {

  import com.github.rabitarochan.play2.angularhelper.InitHelper._

  val form = Form("dummy" -> text)

  describe("FormExtension") {

    describe("Boolean") {
      it("Exist key") {
        val testForm = form.bind(Map("isTrue" -> "true"))
        val actual   = testForm.ngInit("isTrue" -> jsBoolean)
        val expected = JsBoolean(true).toString()
        assert(actual === expected)
      }

      it("Not exist key") {
        val testForm = form.bind(Map("isTrue" -> "true"))
        val actual   = testForm.ngInit("xxx" -> jsBoolean(false))
        val expected = JsBoolean(false).toString()
        assert(actual === expected)
      }

      it("No set key") {
        val testForm = form.bind(Map("xxx" -> "false"))
        val actual   = testForm.ngInit("isTrue" -> jsBoolean(true))
        val expected = JsBoolean(true).toString()
        assert(actual === expected)
      }
    }

    describe("Number property") {
      it("Exist key") {
        val testForm = form.bind(Map("seq" -> "123"))
        val actual   = testForm.ngInit("seq" -> jsNumber)
        val expected = JsNumber(123).toString()
        assert(actual === expected)
      }

      it("Not exist key") {
        val testForm = form.bind(Map("seq" -> "123"))
        val actual   = testForm.ngInit("xxx" -> jsNumber(456))
        val expected = JsNumber(456).toString()
        assert(actual === expected)
      }

      it("No set key") {
        val testForm = form.bind(Map("xxx" -> "123"))
        val actual   = testForm.ngInit("seq" -> jsNumber(789))
        val expected = JsNumber(789).toString()
        assert(actual === expected)
      }
    }

    describe("String property") {
      it("Exist key") {
        val testForm = form.bind(Map("name" -> "test name"))
        val actual   = testForm.ngInit("name" -> jsString)
        val expected = JsString("test name").toString()
        assert(actual === expected)
      }

      it("Not exist key") {
        val testForm = form.bind(Map("name" -> "test name"))
        val actual   = testForm.ngInit("xxx" -> jsString("Default"))
        val expected = JsString("Default").toString()
        assert(actual === expected)
      }

      it("No set key") {
        val testForm = form.bind(Map("xxx" -> "test name"))
        val actual   = testForm.ngInit("name" -> jsString("Default"))
        val expected = JsString("Default").toString()
        assert(actual === expected)
      }
    }

    describe("Object property") {
      it("Exist object") {
        val testForm = form.bind(Map("obj.name" -> "test name", "obj.age" -> "17"))
        val actual   = testForm.ngInit("obj" -> jsObject("name" -> jsString, "age" -> jsNumber))
        val expected = JsObject(Seq("name" -> JsString("test name"), "age" -> JsNumber(17))).toString()
        assert(actual === expected)
      }

      it("Empty object") {
        val actual   = form.ngInit("obj" -> jsObject("name" -> jsString, "age" -> jsNumber))
        val expected = Json.obj("name" -> "", "age" -> 0).toString()
        assert(actual === expected)
      }
    }

    describe("Array property") {
      it("Number array") {
        val testForm = form.bind(Map("numbers[0]" -> "1", "numbers[1]" -> "2", "numbers[2]" -> "3"))
        val actual   = testForm.ngInit("numbers" -> jsArray(jsNumber))
        val expected = JsArray(Seq( JsNumber(1), JsNumber(2), JsNumber(3) )).toString()
        assert(actual === expected)
      }

      it("String array") {
        val testForm = form.bind(Map("strings[1]" -> "aaa", "strings[0]" -> "bbb", "strings[2]" -> "ccc"))
        val actual   = testForm.ngInit("strings" -> jsArray(jsString))
        val expected = JsArray(Seq( JsString("bbb"), JsString("aaa"), JsString("ccc") )).toString()
        assert(actual === expected)
      }

      it("Boolean array") {
        val testForm = form.bind(Map("booleans[2]" -> "false", "booleans[0]" -> "true", "booleans[1]" -> "false"))
        val actual   = testForm.ngInit("booleans" -> jsArray(jsBoolean))
        val expected = JsArray(Seq( JsBoolean(true), JsBoolean(false), JsBoolean(false) )).toString()
        assert(actual === expected)
      }

      it("Object array") {
        val testForm = form.bind(Map(
          "xs[0].name" -> "name_0",
          "xs[1].name" -> "name_1",
          "xs[0].age"  -> "0",
          "xs[1].age"  -> "1"
        ))
        val actual   = testForm.ngInit(
          "xs" -> jsArray(jsObject(
            "name" -> jsString,
            "age"  -> jsNumber
          ))
        )
        val expected = JsArray(Seq(
          JsObject(Seq("name" -> JsString("name_0"), "age" -> JsNumber(0))),
          JsObject(Seq("name" -> JsString("name_1"), "age" -> JsNumber(1)))
        )).toString()
        assert(actual === expected)
      }

      it("Empty array") {
        val actual   = form.ngInit("xs" -> jsArray(jsString))
        val expected = JsArray(Nil).toString()
        assert(actual === expected)
      }

    }

  }

}
