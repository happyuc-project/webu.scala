import com.micronautics.webuj.Huc
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._
import Huc._

@RunWith(classOf[JUnitRunner])
class HucTest extends WordSpec {
  val wei: Huc    = Huc(e(0))
  val kWei: Huc   = Huc(e(3))
  val mWei: Huc   = Huc(e(6))
  val gWei: Huc   = Huc(e(9))
  val TWei: Huc  = Huc(e(12))
  val pwei: Huc = Huc(e(15))
  val huc: Huc  = Huc(e(18))
  val kHuc: Huc = Huc(e(21))
  val mHuc: Huc = Huc(e(24))
  val gHuc: Huc = Huc(e(27))

  "Huc" should {
    "compare" in {
      wei  shouldBe wei
      kWei should be >  wei
      kWei should be >= wei
      wei  should be <  kWei
      wei  should be <= kWei
    }

    "support math" in {
      wei + wei shouldBe Huc(2)
      wei - 1 shouldBe Huc.zero
      wei - 2 shouldBe Huc(-1)
      wei + 2 shouldBe Huc(3)
      wei * 4 shouldBe Huc(4)

      kWei + kWei shouldBe Huc(e(3) * 2)
      kWei - 1000 shouldBe Huc.zero
      kWei - 2000 shouldBe Huc(e(3) * -1)
      kWei + 2000 shouldBe Huc(e(3) * 3)
      kWei * 4    shouldBe Huc(e(3) * 4)
    }

    "convert properly" in {
      Huc.fromWei(BigInt(1))        shouldBe wei
      Huc.fromWei(BigDecimal(1))    shouldBe wei
      Huc.fromWei(1)                shouldBe wei
      Huc.fromWei(1.0)              shouldBe wei
      Huc.fromWei(1.0)              shouldBe wei
      Huc.fromWei(1)                shouldBe wei

      Huc.fromKWei(BigInt(1))       shouldBe kWei
      Huc.fromKWei(BigDecimal(1))   shouldBe kWei
      Huc.fromKWei(1)               shouldBe kWei
      Huc.fromKWei(1.0)             shouldBe kWei
      Huc.fromKWei(1.0)             shouldBe kWei
      Huc.fromKWei(1)               shouldBe kWei

      Huc.fromMWei(BigInt(1))       shouldBe mWei
      Huc.fromMWei(BigDecimal(1))   shouldBe mWei
      Huc.fromMWei(1)               shouldBe mWei
      Huc.fromMWei(1.0)             shouldBe mWei
      Huc.fromMWei(1.0)             shouldBe mWei
      Huc.fromMWei(1)               shouldBe mWei

      Huc.fromGWei(BigInt(1))       shouldBe gWei
      Huc.fromGWei(BigDecimal(1))   shouldBe gWei
      Huc.fromGWei(1)               shouldBe gWei
      Huc.fromGWei(1.0)             shouldBe gWei
      Huc.fromGWei(1.0)             shouldBe gWei
      Huc.fromGWei(1)               shouldBe gWei

      Huc.fromTWei(BigInt(1))      shouldBe TWei
      Huc.fromTWei(BigDecimal(1))  shouldBe TWei
      Huc.fromTWei(1)              shouldBe TWei
      Huc.fromTWei(1.0)            shouldBe TWei
      Huc.fromTWei(1.0)            shouldBe TWei
      Huc.fromTWei(1)              shouldBe TWei

      Huc.fromFinney(BigInt(1))     shouldBe pwei
      Huc.fromFinney(BigDecimal(1)) shouldBe pwei
      Huc.fromFinney(1)             shouldBe pwei
      Huc.fromFinney(1.0)           shouldBe pwei
      Huc.fromFinney(1.0)           shouldBe pwei
      Huc.fromFinney(1)             shouldBe pwei

      Huc.fromHuc(BigInt(1))      shouldBe huc
      Huc.fromHuc(BigDecimal(1))  shouldBe huc
      Huc.fromHuc(1)              shouldBe huc
      Huc.fromHuc(1.0)            shouldBe huc
      Huc.fromHuc(1.0)            shouldBe huc
      Huc.fromHuc(1)              shouldBe huc

      Huc.fromKHuc(BigInt(1))     shouldBe kHuc
      Huc.fromKHuc(BigDecimal(1)) shouldBe kHuc
      Huc.fromKHuc(1)             shouldBe kHuc
      Huc.fromKHuc(1.0)           shouldBe kHuc
      Huc.fromKHuc(1.0)           shouldBe kHuc
      Huc.fromKHuc(1)             shouldBe kHuc

      Huc.fromMHuc(BigInt(1))     shouldBe mHuc
      Huc.fromMHuc(BigDecimal(1)) shouldBe mHuc
      Huc.fromMHuc(1)             shouldBe mHuc
      Huc.fromMHuc(1.0)           shouldBe mHuc
      Huc.fromMHuc(1.0)           shouldBe mHuc
      Huc.fromMHuc(1)             shouldBe mHuc

      Huc.fromGHuc(BigInt(1))     shouldBe gHuc
      Huc.fromGHuc(BigDecimal(1)) shouldBe gHuc
      Huc.fromGHuc(1)             shouldBe gHuc
      Huc.fromGHuc(1.0)           shouldBe gHuc
      Huc.fromGHuc(1.0)           shouldBe gHuc
      Huc.fromGHuc(1)             shouldBe gHuc
    }
  }
}
