import scala.io.Source
import scala.xml.pull._
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.io.FileOutputStream
import scala.xml.XML
import scala.xml._
 

class PostHist(
  id:String,
  postHistoryTypeId:String,
  postId:String,
  revisionGUID: String,
  creationDate: String,
  userId:String,
  closeReasonId:String) {

  def mkString = {
    s"${id};${postHistoryTypeId};${postId};" +
    s"${revisionGUID};${creationDate};${userId};" +
    s"$closeReasonId"
  }
}

val xml = new XMLEventReader(Source.stdin)

def clean(at:Seq[Node]):String = {
  if (at == null) {
    ""
  } else {
    at.mkString.replaceAll(";", "<SEMICOLON>")
  }
}

xml.filter{ event =>
  event match {
    case EvElemStart(_, "row", attrs, _) => true
    case _ => false
  }
}.map { event => 
  event match {
    case EvElemStart(_, _, attrs, _) => {

      new PostHist(
          clean(attrs("Id")),
          clean(attrs("PostHistoryTypeId")),
          clean(attrs("PostId")),

          clean(attrs("RevisionGUID")),
          clean(attrs("CreationDate")),
          clean(attrs("UserId")),

          clean(attrs("CloseReasonId"))
      )
    }
  }
}.foreach{x => println(x.mkString)}
 

