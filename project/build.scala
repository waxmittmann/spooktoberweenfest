import sbt._
import Keys._

object Settings {
  val libgdxVersion2 = "1.9.6"

  lazy val nativeExtractions = SettingKey[Seq[(String, NameFilter, File)]](
    "native-extractions", "(jar name partial, sbt.NameFilter of files to extract, destination directory)"
  )

  lazy val desktopJarName = SettingKey[String]("desktop-jar-name", "name of JAR file for desktop")

  lazy val core = plugins.JvmPlugin.projectSettings ++ Seq(
    version := (version in LocalProject("all-platforms")).value,
    scalaVersion := (scalaVersion in LocalProject("all-platforms")).value,
    libraryDependencies ++= Seq(
      "com.badlogicgames.gdx" % "gdx" % libgdxVersion2,
      "org.specs2" %% "specs2-core" % "3.9.5" % "test",
      "org.specs2" %% "specs2-scalacheck" % "3.9.5" % "test",
      "org.typelevel" %% "cats-core" % "1.0.0-MF"
    ),
    javacOptions ++= Seq(
      "-Xlint",
      "-encoding", "UTF-8",
      "-source", "1.8",
      "-target", "1.8"
    ),
    scalacOptions ++= Seq(
      "-Xlint",
      "-Ywarn-dead-code",
      "-Ywarn-value-discard",
      "-Ywarn-numeric-widen",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-encoding", "UTF-8",
      "-target:jvm-1.8"
    ),
    cancelable := true,
    exportJars := true
  )

  lazy val desktop = core ++ Seq(
    libraryDependencies ++= Seq(
      "net.sf.proguard" % "proguard-base" % "4.11" % "provided",
      "com.badlogicgames.gdx" % "gdx-backend-lwjgl" % libgdxVersion2,
      "com.badlogicgames.gdx" % "gdx-platform" % libgdxVersion2 classifier "natives-desktop",
      "org.specs2" %% "specs2-core" % "3.9.5" % "test",
      "org.typelevel" %% "cats-core" % "1.0.0-MF"
    ),
    fork in Compile := true,
    unmanagedResourceDirectories in Compile += file("android/assets"),
    desktopJarName := "spooktoberweenfest",
    Tasks.assembly
  )
}

object Tasks {
  import Settings.nativeExtractions

  lazy val extractNatives = TaskKey[Unit]("extract-natives", "Extracts native files")

  lazy val natives = Seq(
    ivyConfigurations += config("natives"),
    nativeExtractions := Seq.empty,
    extractNatives <<= (nativeExtractions, update) map { (ne, up) =>
      val jars = up.select(configurationFilter("natives"))
      ne foreach { case (jarName, fileFilter, outputPath) =>
        jars find(_.getName.contains(jarName)) map { jar =>
            IO.unzip(jar, outputPath, fileFilter)
        }
      }
    },
    compile in Compile <<= (compile in Compile) dependsOn (extractNatives)
  )

  import java.io.{File => JFile}
  import Settings.desktopJarName

  lazy val assemblyKey = TaskKey[Unit]("assembly", "Assembly desktop using Proguard")

  lazy val assembly = assemblyKey <<= (fullClasspath in Runtime, // dependency to make sure compile finished
      target, desktopJarName, version, // data for output jar name
      javaOptions in Compile, managedClasspath in Compile, // java options and classpath
      classDirectory in Compile, dependencyClasspath in Compile, update in Compile, // classes and jars to proguard
      streams) map { (c, target, name, ver, options, cp, cd, dependencies, up, s) =>
    val provided = Set(up.select(configurationFilter("provided")):_*)
    val compile = Set(up.select(configurationFilter("compile")):_*)
    val runtime = Set(up.select(configurationFilter("runtime")):_*)
    val optional = Set(up.select(configurationFilter("optional")):_*)
    val onlyProvidedNames = provided -- compile -- runtime -- optional
    val (onlyProvided, withoutProvided) = dependencies.partition(cpe => onlyProvidedNames contains cpe.data)
    val exclusions = Seq("!META-INF/MANIFEST.MF", "!library.properties").mkString(",")
    val inJars = withoutProvided.map("\""+_.data.absolutePath+"\"("+exclusions+")").mkString(JFile.pathSeparator)
    val libraryJars = onlyProvided.map("\""+_.data.absolutePath+"\"").mkString(JFile.pathSeparator)
    val outfile = "\""+(target/"%s-%s.jar".format(name, ver)).absolutePath+"\""
    val classfiles = "\"" + cd.absolutePath + "\""
    val manifest = "\"" + file("desktop/manifest").absolutePath + "\""
    val proguardOptions = scala.io.Source.fromFile(file("core/proguard-project.txt")).getLines.toList ++
                          scala.io.Source.fromFile(file("desktop/proguard-project.txt")).getLines.toList
    val proguard = options ++ Seq("-cp", Path.makeString(cp.files), "proguard.ProGuard") ++ proguardOptions ++ Seq(
      "-injars", classfiles,
      "-injars", inJars,
      "-injars", manifest,
      "-libraryjars", libraryJars,
      "-outjars", outfile)
    s.log.info("preparing proguarded assembly")
    s.log.debug("Proguard command:")
    s.log.debug("java "+proguard.mkString(" "))
    val exitCode = Process("java", proguard) ! s.log
    if (exitCode != 0) {
      sys.error("Proguard failed with exit code [%s]" format exitCode)
    } else {
      s.log.info("Output file: "+outfile)
    }
  }
}

object LibgdxBuild extends Build {
  lazy val core = Project(
    id       = "core",
    base     = file("core"),
    settings = Settings.core
  )

  lazy val desktop = Project(
    id       = "desktop",
    base     = file("desktop"),
    settings = Settings.desktop
  ).dependsOn(core)

  lazy val all = Project(
    id       = "all-platforms",
    base     = file("."),
    settings = Settings.core
  ).aggregate(core, desktop)
}
