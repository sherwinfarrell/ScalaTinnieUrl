package controllers

import javax.inject._
import play.api.http.{HttpErrorHandler, Writeable}
import models.TinnieUrlRepository
import play.api.mvc._
import play.api.libs.json.Json
import utils.ShortenerService

import play.api.Logger
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import views.html.error


@Singleton
class ApplicationController  @Inject()(val errorHandler: HttpErrorHandler, tinnieurl: TinnieUrlRepository, cc: ControllerComponents) extends AbstractController(cc)  {


  val logger: Logger = Logger(this.getClass())

  def index = Action { implicit request =>
    Ok(views.html.index())
  }


  def getShortenedUrl() = Action(parse.json).async{ implicit  request =>

    logger.info("Received Post Request on route /getShortenedUrl")
    val fullUrl = (request.body \ "urlString").as[String]
    logger.info(s"The parsed Json Obj is ${fullUrl}")
    if (fullUrl.contains("https://") || fullUrl.contains("http://")) {
        val idFuture = Await.result(tinnieurl.getId(fullUrl).flatMap(x => x match {
          case Some(dbId) => Future(Some(ShortenerService.encode(dbId)))
          case None => tinnieurl.addUrl(fullUrl).map {
            case Success(value) => Some(ShortenerService.encode(value.id))
            case Failure(exception) => Future.successful(Ok(Json.obj("msg" -> "There with the database call")))
          }
        }), 100.seconds)
        idFuture match {
          case Some(id) => Future.successful(Ok(Json.obj("url" -> f"https://radiant-ravine-94917.herokuapp.com/$id")))
          case _ => Future.successful(Ok(Json.obj("msg" -> "There was an error with the request")))
        }
      }
    else {
      logger.info("The request did not have an appropriate header")
      Future.successful(Ok(Json.obj("msg" -> "The Url does not contain http or https prefixes")))
      }
    }


  def resolveUrl(code: String) = Action.async { implicit request =>
    val result = tinnieurl.getUrl(ShortenerService.resolve(code).toInt)
    Await.result(result.map {
      case Some(urlString) =>
        logger.info("Redirecting the Url to the correct site")
        Future.successful(Redirect(urlString))
      case None =>
        logger.debug("There is a failure: ")
        Future.successful(Ok(views.html.error()))
    }, 30.seconds)
  }
}
