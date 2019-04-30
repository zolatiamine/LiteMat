package Tbox

import org.apache.jena.vocabulary.OWL2

object MainTbox {

  def main(args: Array[String]): Unit = {
    println("test avant")

    val Tbox: TBoxServices = new TBoxServices(FilePath.OwlFiLE, "lubm")
    Tbox.loadModel()

    Tbox.ConcepAction(1L, 0, 1, OWL2.Thing.getURI())
    Tbox.getDictionary().normalizeConcepts()
    Tbox.getDictionary().DisplayC()

    Tbox.PropAction(2L, 0, 2, OWL2.topDataProperty.getURI)
    Tbox.PropAction(3L, 0, 2, OWL2.topObjectProperty.getURI)
    Tbox.getDictionary().normalizeProperties()
    Tbox.getDictionary().DisplayP()


    Tbox.getDictionary().normalizeSubject()

    Tbox.getDictionary().ExportConcToFile(FilePath.SaveFileC)
    Tbox.getDictionary().ExportPropToFile(FilePath.SaveFileP)
    Tbox.getDictionary().ExportSubToFile(FilePath.SaveFileS)



    println("test après")
  }

}
