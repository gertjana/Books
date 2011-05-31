  import net.addictivesoftware.books.web.remote.AmazonApiClient

  object TestApi {

  def test(){

    val client = new AmazonApiClient()

    val result = client.getBookInformationForIsbn("0812548094")

    Console.out.println(result);

  }
}



