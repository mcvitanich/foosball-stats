import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "playApplication"
  val appVersion = "1.0"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa,
    cache,
    "org.slf4j" % "log4j-over-slf4j" % "1.7.6",
    "org.springframework" % "spring-context" % "4.0.3.RELEASE",
    "commons-io" % "commons-io" % "2.4",
    "org.projectlombok" % "lombok" % "1.16.14",
    "org.hamcrest" % "hamcrest-all" % "1.3",
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",
    "org.json" % "json" % "20090211",
    "org.mockito" % "mockito-core" % "1.10.19"
  )

  val main = play.Project(appName, appVersion, appDependencies)
    .settings(projectSettings: _*)
    .settings(resolvers ++= Seq(
      "Nexus Staging Repo" at "http://git.ml.com:8081/nexus/content/repositories/MLGrailsPlugins",
      "ML Arquitectura" at "http://git.ml.com:8081/nexus/content/groups/Arquitectura/",
      "Jboss repo" at "https://repository.jboss.org/nexus/content/groups/public",
      "Spy Repository" at "http://files.couchbase.com/maven2",
      "meli" at "http://git.ml.com/nexus/content/groups/ML"))
    .settings(Keys.fork in (Test) := true,
      // uncomment this line to enable remote debugging
      //javaOptions in (Test) += "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9998",
      javaOptions in (Test) += "-Xms512M",
      javaOptions in (Test) += "-Xmx2048M",
      javaOptions in (Test) += "-Xss1M")

}