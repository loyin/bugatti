package controllers.admin

import controllers.BaseController
import models.logs.{Logs, LogsHelper}
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._
import play.api.mvc._

/**
 * log日志
 */
object LogsController extends BaseController {

  implicit val writer = new Writes[(Int, String, String)] {
    def writes(c: (Int, String, String)): JsValue = {
      Json.obj("id" -> c._1, "time" -> c._2, "info" -> c._3)
    }
  }

  val logsForm = Form(
    mapping(
      "jobNo" -> optional(text),
      "mode" -> optional(text),
      "startTime" -> jodaDate("yyyy-MM-dd HH:mm:ss"),
      "endTime" -> jodaDate("yyyy-MM-dd HH:mm:ss")
    )(Logs.apply)(Logs.unapply)
  )

  def search(page: Int, pageSize: Int) = Action { implicit request =>
    logsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(formWithErrors.errorsAsJson),
      logs => {
        Ok(Json.toJson(LogsHelper.searchLogs(logs, page, pageSize)))
      }
    )
  }

  def count = Action { implicit request =>
    logsForm.bindFromRequest.fold(
      formWithErrors => BadRequest(formWithErrors.errorsAsJson),
      logs => {
        Ok(Json.toJson(LogsHelper.searchCount(logs)))
      }
    )
  }

}
