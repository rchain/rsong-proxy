package coop.rchain.service

import com.typesafe.scalalogging.Logger
import coop.rchain.utils.IdGen
import org.specs2._

class UserServiceSpec extends Specification {
  def is = s2"""
   user service specs
       new user rholang terms correctly $newUserRhoBuild
       playcount rholang terms correctly $playCountRhorBuild
    """
  import coop.rchain.service.UserService._
  val log = Logger[UserServiceSpec]

  def newUserRhoBuild = {
    val computed: String = newUserRhoTerm(IdGen.uuid)
    log.info(s"term-spec : ${computed}")
    computed.isEmpty === false
  }

  def playCountRhorBuild = {
    val computed: String = playCountRhoTerm("john-smith")
    log.info(s"term-spec : ${computed}")
    computed.isEmpty === false
  }
}