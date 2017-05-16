import scala.io.Source
import scala.xml.pull._
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.io.FileOutputStream
import scala.xml.XML
import scala.xml._
 
val xml = new XMLEventReader(Source.stdin)

def clean(at:Seq[Node]):String = {
  if (at == null) {
    "<NULL>"
  } else {
    at.mkString.replaceAll(";", "<SEMICOLON>").replaceAll("\n", "<LN>")
  }
}

val users = xml.filter{ event =>
  event match {
    case EvElemStart(_, "row", attrs, _) => true
    case _ => false
  }
}.map { event => 
  event match {
    case EvElemStart(_, _, attrs, _) => {

      val impo = (
        clean(attrs("Id")),
        clean(attrs("Reputation")),
        clean(attrs("CreationDate")),
        clean(attrs("DisplayName")),
        clean(attrs("EmailHash")),
        clean(attrs("LastAccessDate")),
        clean(attrs("WebsiteUrl")),
        clean(attrs("Location")),
        clean(attrs("Age")),
        clean(attrs("Views")),
        clean(attrs("UpVotes")),
        clean(attrs("DownVotes"))
      )
      println(impo.productIterator.mkString(";"))
    }
  }
}
 
System.err.println(users.length)

