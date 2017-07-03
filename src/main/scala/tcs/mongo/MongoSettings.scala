package tcs.mongo

/**
  * Created by stefano on 13/06/17.
  */
class MongoSettings(val database: String,
                    val host: String = "127.0.0.1",
                    val port: String = "27017",
                    val user: String = "",
                    val psw: String = ""
                   ) {
}
