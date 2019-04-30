# LiteMat

# Ce present projet est réaliseé par :
    * ZOLATI Mohamed Amine
    * BENCHAABOUNE Mohammmed
    
# dependencies utilisé 

libraryDependencies += "org.phenoscape" %% "scowl" % "1.3"

libraryDependencies += "org.apache.jena" % "apache-jena" % "3.0.1" pomOnly()

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0"

libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"

dependencyOverrides += "com.google.guava" % "guava" % "15.0"


# Pour éviter certains erreurs nous avons ajouté L'outils winutils. Dans le main il faut ajouter la ligne suivante:
System.setProperty("hadoop.home.dir", "Path//winutils")

