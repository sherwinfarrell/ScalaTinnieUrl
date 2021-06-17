package models

import javax.inject.Inject
import play.api.{Logger, Logging}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.basic.DatabaseConfig
import slick.dbio.{DBIOAction, Effect}
import slick.jdbc.JdbcProfile
import slick.jdbc.PostgresProfile.api._
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.{Duration, DurationInt}
import scala.util.{Failure, Success, Try}


case class UrlTracker(id:Int, url:String)

class UrlTrackerTableDef(tag: Tag) extends Table[UrlTracker](tag, "urltracker") {

  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def url = column[String]("url", O.PrimaryKey)

  override def * =
    (id, url) <>(UrlTracker.tupled, UrlTracker.unapply)
}


class TinnieUrlRepository @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] with Logging {

  val tinnieUrl = TableQuery[UrlTrackerTableDef]
  val df = dbConfig.db

  override val logger: Logger = Logger(this.getClass())


  def addUrl(inputUrl: String) : Future[Try[UrlTracker]] = {
    val query = tinnieUrl returning tinnieUrl.map(_.id) into ((x, id) => x.copy(id = id))
    val action = query += UrlTracker(-1, inputUrl)
    db.run(action.asTry)

  }

  def getUrl(id: Int)  = {
    val query = tinnieUrl.filter(_.id === id)
    db.run(query.result.asTry).map{
      case Success(x) =>
        logger.debug("Get Url Db Call was Successfull")
        x.headOption.map(_.url)
      case Failure(s) => logger.debug(s"There was an error with the database call: ${s}")
        None
    }

  }

  def getId(url:String) : Future[Option[Int]] = {
    val query = tinnieUrl.filter(_.url === url)
    val action = query.result
    db.run(action.asTry).map{
      case Success(x) =>
        logger.debug("Get Id db call was successful")
        x.headOption.map(_.id)
      case Failure(s) => logger.debug("There was an error with the database call: ${s}")
        None

    }

  }

}