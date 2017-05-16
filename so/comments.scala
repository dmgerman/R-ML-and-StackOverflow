import scala.io.Source
import scala.xml.pull._
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.io.FileOutputStream
import scala.xml.XML
import scala.xml._
 

class Comment(
  id:String, postId:String, score:String,
  creationDate: String, userId: String
  ) {

  def mkString = {
    s"${id};${postId};${score};" +
    s"${creationDate};${userId}"
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

      new Comment(
          clean(attrs("Id")),
          clean(attrs("PostId")),
          clean(attrs("Score")),

          clean(attrs("CreationDate")),
          clean(attrs("UserId"))
      )
    }
  }
}.foreach{x => println(x.mkString)}
 

