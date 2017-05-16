import scala.io.Source
import scala.xml.pull._
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.io.FileOutputStream
import scala.xml.XML
import scala.xml._
 

class PostLinks(
  id:String,
  creationDate: String,
  postId:String,
  relatedPostId:String,
  linkTypeId:String) {

  def mkString = {
    s"${id};${creationDate};${postId};" +
    s"${relatedPostId};${linkTypeId}" 
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

      new PostLinks(
          clean(attrs("Id")),
          clean(attrs("CreationDate")),
          clean(attrs("PostId")),

          clean(attrs("RelatedPostId")),
          clean(attrs("LinkTypeId"))
      )
    }
  }
}.foreach{x => println(x.mkString)}
 

