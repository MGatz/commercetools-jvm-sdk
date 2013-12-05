package sphere

import com.google.common.collect.ImmutableMap
import io.sphere.client.model.{Money, LocalizedString}
import io.sphere.client.shop.{SphereClient, SphereClientConfig}
import java.util.{UUID, List, Currency, Locale}
import scala.util.Properties._
import org.scalatest.matchers.{MatchResult, Matcher}
import io.sphere.client.shop.model.{Address, CartUpdate, Product}
import sphere.IntegrationTest.Implicits._
import scala.collection.JavaConversions._
import com.neovisionaries.i18n.CountryCode.DE

object IntegrationTest {
  object Implicits {
    implicit lazy val EUR = Currency.getInstance("EUR")
    implicit def string2localizedString(s: String): LocalizedString =
      new LocalizedString(ImmutableMap.of(Locale.ENGLISH, s, Locale.FRENCH, s"le ${s}"))

    implicit val locale = Locale.ENGLISH

    implicit final class MoneyNotation(val currency: Currency) extends AnyVal {
      def apply(amount: String): Money = new Money(new java.math.BigDecimal(amount), currency.getCurrencyCode)
    }
  }

  object beSimilar {
    def apply[T](right: T, keyFn: (T => Any)*) =  new Matcher[T] {
      def apply(left: T): MatchResult = {
        val results: Seq[MatchResult] = keyFn.map(fn => (fn(left), fn(right))).map {
          case (a, b) =>
            new MatchResult(a == b, s"$a did not equal $b", s"$a equaled $b")
        }
        val errors = results.filterNot(_.matches)
        if(errors.size > 0) errors(0)
        else new MatchResult(true, s"$left did not equal $right", s"$left equaled $right")
      }
    }
  }

  def randomString() = UUID.randomUUID.toString
}

object TestData {
  def allProducts(implicit client: SphereClient): List[Product] = client.products().all().fetch().getResults
  def oneProduct(implicit client: SphereClient) = allProducts(client)(0)
  def newCart(implicit client: SphereClient) = client.carts().createCart(EUR).execute()
  def newCartWithProduct(implicit client: SphereClient) = {
    val cart = newCart
    val update = new CartUpdate().addLineItem(1, oneProduct(client).getId).setShippingAddress(GermanAddress)
    client.carts().updateCart(cart.getIdAndVersion, update).execute()
  }
  def newOrderOf1Product(implicit client: SphereClient) = {
    client.orders().createOrder(newCartWithProduct.getIdAndVersion).execute()
  }
  val GermanAddress: Address = new Address(DE)
}

object IntegrationTestClient {
  def apply() = {
    val Seq(projectKey,clientId,clientSecret) = getConfiguration
    val builder = new SphereClientConfig.Builder(projectKey, clientId, clientSecret, Locale.ENGLISH)
    envOrNone("SDK_IT_SERVICE_URL").map(builder.setCoreHttpServiceUrl(_))
    envOrNone("SDK_IT_AUTH_URL").map(builder.setAuthHttpServiceUrl(_))
    SphereClient.create(builder.build)
  }

  def getConfiguration: Seq[String] = {
    val values = Seq("SDK_IT_PROJECT_KEY", "SDK_IT_CLIENT_ID", "SDK_IT_CLIENT_SECRET").map(key => (key, envOrNone(key)))
    val missingValues = values.filterNot(_._2.isDefined)
    if (!missingValues.isEmpty) {
      throw new RuntimeException(s"Missing environment arguments: ${missingValues.map(_._1).mkString(",")}")
    }
    val Seq(projectKey, clientId, clientSecret) = values.map(_._2.get)
    Seq(projectKey, clientId, clientSecret)
  }
}