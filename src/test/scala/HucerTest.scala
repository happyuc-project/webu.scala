import com.micronautics.webuj.Hucer
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest._
import org.scalatest.Matchers._
import Hucer._

@RunWith(classOf[JUnitRunner])
class HucerTest extends WordSpec {
  val wei: Hucer    = Hucer(e(0))
  val kWei: Hucer   = Hucer(e(3))
  val mWei: Hucer   = Hucer(e(6))
  val gWei: Hucer   = Hucer(e(9))
  val szabo: Hucer  = Hucer(e(12))
  val finney: Hucer = Hucer(e(15))
  val ether: Hucer  = Hucer(e(18))
  val kHucer: Hucer = Hucer(e(21))
  val mHucer: Hucer = Hucer(e(24))
  val gHucer: Hucer = Hucer(e(27))

  "Hucer" should {
    "compare" in {
      wei  shouldBe wei
      kWei should be >  wei
      kWei should be >= wei
      wei  should be <  kWei
      wei  should be <= kWei
    }

    "support math" in {
      wei + wei shouldBe Hucer(2)
      wei - 1 shouldBe Hucer.zero
      wei - 2 shouldBe Hucer(-1)
      wei + 2 shouldBe Hucer(3)
      wei * 4 shouldBe Hucer(4)

      kWei + kWei shouldBe Hucer(e(3) * 2)
      kWei - 1000 shouldBe Hucer.zero
      kWei - 2000 shouldBe Hucer(e(3) * -1)
      kWei + 2000 shouldBe Hucer(e(3) * 3)
      kWei * 4    shouldBe Hucer(e(3) * 4)
    }

    "convert properly" in {
      Hucer.fromWei(BigInt(1))        shouldBe wei
      Hucer.fromWei(BigDecimal(1))    shouldBe wei
      Hucer.fromWei(1)                shouldBe wei
      Hucer.fromWei(1.0)              shouldBe wei
      Hucer.fromWei(1.0)              shouldBe wei
      Hucer.fromWei(1)                shouldBe wei

      Hucer.fromKWei(BigInt(1))       shouldBe kWei
      Hucer.fromKWei(BigDecimal(1))   shouldBe kWei
      Hucer.fromKWei(1)               shouldBe kWei
      Hucer.fromKWei(1.0)             shouldBe kWei
      Hucer.fromKWei(1.0)             shouldBe kWei
      Hucer.fromKWei(1)               shouldBe kWei

      Hucer.fromMWei(BigInt(1))       shouldBe mWei
      Hucer.fromMWei(BigDecimal(1))   shouldBe mWei
      Hucer.fromMWei(1)               shouldBe mWei
      Hucer.fromMWei(1.0)             shouldBe mWei
      Hucer.fromMWei(1.0)             shouldBe mWei
      Hucer.fromMWei(1)               shouldBe mWei

      Hucer.fromGWei(BigInt(1))       shouldBe gWei
      Hucer.fromGWei(BigDecimal(1))   shouldBe gWei
      Hucer.fromGWei(1)               shouldBe gWei
      Hucer.fromGWei(1.0)             shouldBe gWei
      Hucer.fromGWei(1.0)             shouldBe gWei
      Hucer.fromGWei(1)               shouldBe gWei

      Hucer.fromSzabo(BigInt(1))      shouldBe szabo
      Hucer.fromSzabo(BigDecimal(1))  shouldBe szabo
      Hucer.fromSzabo(1)              shouldBe szabo
      Hucer.fromSzabo(1.0)            shouldBe szabo
      Hucer.fromSzabo(1.0)            shouldBe szabo
      Hucer.fromSzabo(1)              shouldBe szabo

      Hucer.fromFinney(BigInt(1))     shouldBe finney
      Hucer.fromFinney(BigDecimal(1)) shouldBe finney
      Hucer.fromFinney(1)             shouldBe finney
      Hucer.fromFinney(1.0)           shouldBe finney
      Hucer.fromFinney(1.0)           shouldBe finney
      Hucer.fromFinney(1)             shouldBe finney

      Hucer.fromHucer(BigInt(1))      shouldBe ether
      Hucer.fromHucer(BigDecimal(1))  shouldBe ether
      Hucer.fromHucer(1)              shouldBe ether
      Hucer.fromHucer(1.0)            shouldBe ether
      Hucer.fromHucer(1.0)            shouldBe ether
      Hucer.fromHucer(1)              shouldBe ether

      Hucer.fromKHucer(BigInt(1))     shouldBe kHucer
      Hucer.fromKHucer(BigDecimal(1)) shouldBe kHucer
      Hucer.fromKHucer(1)             shouldBe kHucer
      Hucer.fromKHucer(1.0)           shouldBe kHucer
      Hucer.fromKHucer(1.0)           shouldBe kHucer
      Hucer.fromKHucer(1)             shouldBe kHucer

      Hucer.fromMHucer(BigInt(1))     shouldBe mHucer
      Hucer.fromMHucer(BigDecimal(1)) shouldBe mHucer
      Hucer.fromMHucer(1)             shouldBe mHucer
      Hucer.fromMHucer(1.0)           shouldBe mHucer
      Hucer.fromMHucer(1.0)           shouldBe mHucer
      Hucer.fromMHucer(1)             shouldBe mHucer

      Hucer.fromGHucer(BigInt(1))     shouldBe gHucer
      Hucer.fromGHucer(BigDecimal(1)) shouldBe gHucer
      Hucer.fromGHucer(1)             shouldBe gHucer
      Hucer.fromGHucer(1.0)           shouldBe gHucer
      Hucer.fromGHucer(1.0)           shouldBe gHucer
      Hucer.fromGHucer(1)             shouldBe gHucer
    }
  }
}
