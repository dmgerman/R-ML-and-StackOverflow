import scala.io.Source
import scala.xml.pull._
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.io.FileOutputStream
import scala.xml.XML
import scala.xml._
 

class Post(
  id:String, postTypeId:String, parentId:String,
  acceptedId: String, creationDate: String, score:String,
  viewCount:String,   ownerUserId: String, answerCount:String,
  commentCount:String,   favoriteCount: String) {

  def mkString = {
    s"${id};${postTypeId};${parentId};" +
    s"${acceptedId};${creationDate};${score};" +
    s"${viewCount};$ownerUserId;$answerCount;" +
    s"$commentCount;$favoriteCount"
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

      new Post(
          clean(attrs("Id")),
          clean(attrs("PostTypeId")),
          clean(attrs("ParentId")),

          clean(attrs("AcceptedAnswerId")),
          clean(attrs("CreationDate")),
          clean(attrs("Score")),

          clean(attrs("ViewCount")),
          clean(attrs("OwnerUserId")),
          clean(attrs("AnswerCount")),

          clean(attrs("CommentCount")),
          clean(attrs("FavoriteCount"))
      )
    }
  }
}.foreach{x => println(x.mkString)}
 

