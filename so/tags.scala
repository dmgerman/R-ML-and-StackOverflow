import scala.io.Source
import scala.xml.pull._
import scala.collection.mutable.ArrayBuffer
import java.io.File
import java.io.FileOutputStream
import scala.xml.XML
import scala.xml._
 
val xml = new XMLEventReader(Source.stdin)
println(xml)
var insidePage = false
var buf = ArrayBuffer[String]()

def clean(at:Seq[Node]):String = {
  if (at == null) {
    "<NULL>"
  } else {
    at.mkString.replaceAll(";", "<SEMICOLON>")
  }
}

val tags = xml.filter{ event =>
  event match {
    case EvElemStart(_, "row", attrs, _) => true
    case _ => false
  }
}.map { event =>
  event match {
    case EvElemStart(_, _, attrs, _) => {

      val impo = (
        clean(attrs("Id")),
        clean(attrs("TagName")),
        clean(attrs("Count"))
      )
      println(impo.productIterator.mkString(";"))
    }
  }
}
 
System.err.println(tags.length)

