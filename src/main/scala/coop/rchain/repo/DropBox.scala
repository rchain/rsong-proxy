package coop.rchain.repo

import com.dropbox.core.DbxException
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.ListFolderResult
import coop.rchain.utils.Globals._

object DropBox {

  val isrcList = (
    "rcisrc001" -> "Tiny_Human_Sterio.izr",
    "rcisrc002" -> "Tiny_Human_Sterio.izr",
    )
  val client: DbxClientV2  = new DbxClientV2(
    DbxRequestConfig.newBuilder("dropbox/immersion-rchain-proxy").build,
    appCfg.getString("dropbox.access_token") )
  val account = client.users.getCurrentAccount
}
