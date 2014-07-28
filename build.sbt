name := "github-issues"

organization  := "ninja.fangs"

version := "1.0-SNAPSHOT"

scalacOptions ++= Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "ch.qos.logback"    %  "logback-classic"  % "1.0.9",
  "com.typesafe.akka" %% "akka-slf4j"       % "2.2.3",
  "com.typesafe.akka" %% "akka-remote"      % "2.2.3",
  "org.scalafx"       %% "scalafx"          % "2.2.60-R9",
  "org.scalaj"        %% "scalaj-http"      % "0.3.10",
  "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5")

unmanagedJars in Compile += Attributed.blank(file(sys.env.getOrElse("JAVA_HOME", "/usr/lib/jvm/jdk1.7.0_45") + "/jre/lib/jfxrt.jar"))

fork in run := true
