package services

import com.gu.facia.client.models.{Branded, CollectionConfigJson, ConfigJson, FrontJson}
import model.{ApplicationContext, ApplicationIdentity}
import org.scalatest.{FlatSpec, Matchers}
import play.api.Environment
import test.WithTestContext

class ShouldServeFrontTest extends FlatSpec with Matchers with WithTestContext {

  val fronts = ConfigJson(
    Map(
      "email-front" -> FrontJson(
        collections = List("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        navSection = None,
        webTitle = None,
        title = None,
        description = None,
        onPageDescription = None,
        imageUrl = None,
        imageWidth = None,
        imageHeight = None,
        isImageDisplayed = None,
        priority = Some("email"),
        isHidden = None,
        canonical = Some("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        group = Some("US professional")
      ),
      "hidden-email-front" -> FrontJson(
        collections = List("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        navSection = None,
        webTitle = None,
        title = None,
        description = None,
        onPageDescription = None,
        imageUrl = None,
        imageWidth = None,
        imageHeight = None,
        isImageDisplayed = None,
        priority = Some("email"),
        isHidden = Some(true),
        canonical = Some("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        group = Some("US professional")
      ),
      "editorial-front" -> FrontJson(
        collections = List("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        navSection = None,
        webTitle = None,
        title = None,
        description = None,
        onPageDescription = None,
        imageUrl = None,
        imageWidth = None,
        imageHeight = None,
        isImageDisplayed = None,
        priority = Some("editorial"),
        isHidden = None,
        canonical = Some("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        group = Some("US professional")
      ),
      "hidden-editorial-front" -> FrontJson(
        collections = List("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        navSection = None,
        webTitle = None,
        title = None,
        description = None,
        onPageDescription = None,
        imageUrl = None,
        imageWidth = None,
        imageHeight = None,
        isImageDisplayed = None,
        priority = Some("editorial"),
        isHidden = Some(true),
        canonical = Some("e59785e9-ba82-48d8-b79a-0a80b2f9f808"),
        group = Some("US professional")
      )
    ),
    Map(
      "e59785e9-ba82-48d8-b79a-0a80b2f9f808" -> CollectionConfigJson(
        displayName = Some("sc johnson partner zone"),
        backfill = None,
        metadata = Some(List(Branded)),
        `type` = Some("fixed/large/slow-XIV"),
        href = None,
        description = None,
        groups = None,
        uneditable = None,
        showTags = None,
        showSections = None,
        hideKickers = None,
        showDateHeader = None,
        showLatestUpdate = None,
        excludeFromRss = None,
        showTimestamps = None,
        hideShowMore = None
      )
    )
  )

  ConfigAgent.refreshWith(fronts)

  "shouldServeFront" should "not serve the front if the front is not in the config JSON" in {
    ConfigAgent.shouldServeFront("nonexistent-front") should be(false)
  }

  it should "not serve a hidden editorial front" in {
    ConfigAgent.shouldServeFront("hidden-editorial-front") should be(false)
  }

  it should "serve a hidden front in preview mode" in {
    val previewContext = ApplicationContext(Environment.simple(), ApplicationIdentity("preview"))
    ConfigAgent.shouldServeFront("editorial-front")(previewContext) should be(true)
  }

  it should "not serve an email front requested as a normal front" in {
    ConfigAgent.shouldServeFront("email-front") should be(false)
  }

  it should "serve an email front requested as an email front" in {
    ConfigAgent.shouldServeFront("email-front", isEmailRequest = true) should be(true)
  }

  it should "not serve a hidden email front requested as an email front" in {
    ConfigAgent.shouldServeFront("hidden-email-front", isEmailRequest = true) should be(false)
  }

}