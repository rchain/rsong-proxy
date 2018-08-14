package coop.rchain.repo

import com.dropbox.core.v2.files.ListFolderResult
import scala.collection.JavaConverters._
import org.specs2._
/**
class DropBoxSpec extends  Specification { def is =s2"""
    DropBox Repository Specification
      should verify dropbox account by fetcing a list of files/folders $e1
    """
  import DropBox._
  def e1 = {
    val myFolders: ListFolderResult = client.files.listFolder("/assets")
    val folders = myFolders.getEntries.listIterator.asScala.toList

    folders.foreach(x => println( s"${x.getPathLower}") )
    folders.isEmpty === false
  }

}
 * */
