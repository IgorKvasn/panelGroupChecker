package checker

import scala.xml.XML
import java.io.File


/**
 * @author Igo
 */
object Checker {
  var badFiles = 0

  def main(args: Array[String]) {
    if (args.isEmpty) {
      println("You have to run this program with list of directories you want to scan as argument.")
    } else {
      args.foreach(d=>{
        println("Scanning directory: " + d)
        walkDir(d)
      })

      println()

      if (badFiles==0){
        println("Scanning done - nothing found")
      }else{
        println("Scanning done - found problems: " + badFiles + "(see above for list of bad files)")
      }
    }
  }

  def walkDir(path: File): Unit =
    if (path.isFile) {
      checkPanelGroup(path)
    } else {
      path.listFiles().foreach(walkDir)
    }


  def checkPanelGroup(file: File) {
    if (file.getName.endsWith("xhtml")) {
      val xml = XML.loadFile(file)

      val panelGroups = xml \\ "panelGroup"

      if (panelGroups.count(group => {
        group.attribute("layout") match {
          case Some(l) => {
            !"block".equals(l.toString())
          }
          case None => true
        }
      }) > 0) {
        badFiles += 1
        println(file)
      }
    }
  }

  private implicit def string2File(s: String): File = new File(s)
}
