package com.micronautics.webuj

object Hucer {
  lazy val zero: Hucer = Hucer(0)

  /** @return a BigInt containing 1 with the given number of zeros after it, e.g. {{{e(3) == BigInt(1000)}}} */
  @inline def e(howManyZeros: Int): BigInt = BigInt(s"1${ zeros(howManyZeros) }")

  /** @return a String containing the specified number of zeros, e.g. {{{zeros(3) == "000"}}} */
  @inline def zeros(howMany: Int): String = "0" * howMany

  /** @param value unit is wei */
  @inline def apply(value: String):          Hucer = new Hucer(BigInt(value))
  @inline def apply(value: BigInt):          Hucer = new Hucer(value)
  @inline def apply(value: Int):             Hucer = new Hucer(BigInt(value))
  @inline def apply(value: Double):          Hucer = new Hucer(BigDecimal(value).bigDecimal.toBigInteger)
  @inline def apply(value: BigDecimal):      Hucer = new Hucer(value.bigDecimal.toBigInteger)

  @inline def fromWei(value: String):        Hucer = Hucer(value)
  @inline def fromWei(value: Double):        Hucer = Hucer(value)
  @inline def fromWei(value: BigDecimal):    Hucer = Hucer(value)
  @inline def fromWei(value: Int):           Hucer = Hucer(value)
  @inline def fromWei(value: BigInt):        Hucer = Hucer(value)

  @inline def fromKWei(value: String):       Hucer = Hucer(value + zeros(3))
  @inline def fromKWei(value: Double):       Hucer = Hucer(BigDecimal(value) * BigDecimal(e(3)))
  @inline def fromKWei(value: BigDecimal):   Hucer = Hucer(value * BigDecimal(e(3)))
  @inline def fromKWei(value: Int):          Hucer = Hucer(value * e(3))
  @inline def fromKWei(value: BigInt):       Hucer = Hucer(value * e(3))

  @inline def fromMWei(value: String):       Hucer = Hucer(value + zeros(6))
  @inline def fromMWei(value: Double):       Hucer = Hucer(BigDecimal(value) * BigDecimal(e(6)))
  @inline def fromMWei(value: BigDecimal):   Hucer = Hucer(value * BigDecimal(e(6)))
  @inline def fromMWei(value: Int):          Hucer = Hucer(value * e(6))
  @inline def fromMWei(value: BigInt):       Hucer = Hucer(value * e(6))

  @inline def fromGWei(value: String):       Hucer = Hucer(value + zeros(9))
  @inline def fromGWei(value: Double):       Hucer = Hucer(BigDecimal(value) * BigDecimal(e(9)))
  @inline def fromGWei(value: BigDecimal):   Hucer = Hucer(value * BigDecimal(e(9)))
  @inline def fromGWei(value: Int):          Hucer = Hucer(value * e(9))
  @inline def fromGWei(value: BigInt):       Hucer = Hucer(value * e(9))

  @inline def fromSzabo(value: String):      Hucer = Hucer(value + zeros(12))
  @inline def fromSzabo(value: Double):      Hucer = Hucer(BigDecimal(value) * BigDecimal(e(12)))
  @inline def fromSzabo(value: BigDecimal):  Hucer = Hucer(value * BigDecimal(e(12)))
  @inline def fromSzabo(value: Int):         Hucer = Hucer(value * e(12))
  @inline def fromSzabo(value: BigInt):      Hucer = Hucer(value * e(12))

  @inline def fromFinney(value: String):     Hucer = Hucer(value + zeros(15))
  @inline def fromFinney(value: Double):     Hucer = Hucer(BigDecimal(value) * BigDecimal(e(15)))
  @inline def fromFinney(value: BigDecimal): Hucer = Hucer(value * BigDecimal(e(15)))
  @inline def fromFinney(value: Int):        Hucer = Hucer(value * e(15))
  @inline def fromFinney(value: BigInt):     Hucer = Hucer(value * e(15))

  @inline def fromHucer(value: String):      Hucer = Hucer(value + zeros(18))
  @inline def fromHucer(value: Double):      Hucer = Hucer(BigDecimal(value) * BigDecimal(e(18)))
  @inline def fromHucer(value: BigDecimal):  Hucer = Hucer(value * BigDecimal(e(18)))
  @inline def fromHucer(value: Int):         Hucer = Hucer(value * e(18))
  @inline def fromHucer(value: BigInt):      Hucer = Hucer(value * e(18))

  @inline def fromKHucer(value: String):     Hucer = Hucer(value + zeros(21))
  @inline def fromKHucer(value: Double):     Hucer = Hucer(BigDecimal(value) * BigDecimal(e(21)))
  @inline def fromKHucer(value: BigDecimal): Hucer = Hucer(value * BigDecimal(e(21)))
  @inline def fromKHucer(value: Int):        Hucer = Hucer(value * e(21))
  @inline def fromKHucer(value: BigInt):     Hucer = Hucer(value * e(21))

  @inline def fromMHucer(value: String):     Hucer = Hucer(value + zeros(24))
  @inline def fromMHucer(value: Double):     Hucer = Hucer(BigDecimal(value) * BigDecimal(e(24)))
  @inline def fromMHucer(value: BigDecimal): Hucer = Hucer(value * BigDecimal(e(24)))
  @inline def fromMHucer(value: Int):        Hucer = Hucer(value * e(24))
  @inline def fromMHucer(value: BigInt):     Hucer = Hucer(value * e(24))

  @inline def fromGHucer(value: String):     Hucer = Hucer(value + zeros(27))
  @inline def fromGHucer(value: Double):     Hucer = Hucer(BigDecimal(value) * BigDecimal(e(27)))
  @inline def fromGHucer(value: BigDecimal): Hucer = Hucer(value * BigDecimal(e(27)))
  @inline def fromGHucer(value: Int):        Hucer = Hucer(value * e(27))
  @inline def fromGHucer(value: BigInt):     Hucer = Hucer(value * e(27))
}

/** Wei are the smallest unit of currency and are always integers, never fractional quantities */
class Hucer(val wei: BigInt) extends Ordered[Hucer] {
  import Hucer._

  @inline def *(value: Hucer): Hucer      = Hucer(wei * value.wei)
  @inline def *(value: Int): Hucer        = Hucer(wei * value)
  @inline def *(value: Double): Hucer     = Hucer(BigDecimal(wei) * value)
  @inline def *(value: BigInt): Hucer     = Hucer(value) * wei
  @inline def *(value: BigDecimal): Hucer = Hucer(value) * wei

  @inline def /(value: Hucer): Hucer      = Hucer(wei / value.wei)
  @inline def /(value: Int): Hucer        = Hucer(wei / value)
  @inline def /(value: Double): Hucer     = Hucer(BigDecimal(wei) / value)
  @inline def /(value: BigInt): Hucer     = Hucer(value) / wei
  @inline def /(value: BigDecimal): Hucer = Hucer(value) / wei

  @inline def +(value: Hucer): Hucer      = Hucer(wei + value.wei)
  @inline def +(value: Int): Hucer        = Hucer(wei + value)
  @inline def +(value: Double): Hucer     = Hucer(BigDecimal(wei) + value)
  @inline def +(value: BigInt): Hucer     = Hucer(value) + wei
  @inline def +(value: BigDecimal): Hucer = Hucer(value) + wei

  @inline def -(value: Hucer): Hucer      = Hucer(wei - value.wei)
  @inline def -(value: Int): Hucer        = Hucer(wei - value)
  @inline def -(value: Double): Hucer     = Hucer(BigDecimal(wei) - value)
  @inline def -(value: BigInt): Hucer     = Hucer(value) - wei
  @inline def -(value: BigDecimal): Hucer = Hucer(value) - wei

  @inline def bigInteger: java.math.BigInteger = wei.bigInteger
  @inline def asWei: BigInt        = wei
  @inline def asKWei: BigDecimal   = bigDecimal(wei / e(3))
  @inline def asMWei: BigDecimal   = bigDecimal(wei / e(6))
  @inline def asGWei: BigDecimal   = bigDecimal(wei / e(9))
  @inline def asSzabo: BigDecimal  = bigDecimal(wei / e(12))
  @inline def asFinney: BigDecimal = bigDecimal(wei / e(15))
  @inline def asHucer: BigDecimal  = bigDecimal(wei / e(18))
  @inline def asKHucer: BigDecimal = bigDecimal(wei / e(21))
  @inline def asMHucer: BigDecimal = bigDecimal(wei / e(24))
  @inline def asGHucer: BigDecimal = bigDecimal(wei / e(27))

  /** @return Amount of Hucer corresponding to the given wei value */
  @inline def bigDecimal(wei: BigInt): java.math.BigDecimal =
    new java.math.BigDecimal(wei.bigInteger)
      .setScale(16, java.math.BigDecimal.ROUND_DOWN)

  @inline def compare(that: Hucer): Int = this.wei compare that.wei

  @inline override def equals(that: Any): Boolean =
    that match {
      case that: Hucer =>
        this.hashCode == that.hashCode

      case _ => false
    }

  @inline override def hashCode: Int = wei.hashCode

  @inline def isNegative: Boolean = wei < zero.wei

  /** Zero is not considered to be a positive value */
  @inline def isPositive: Boolean = wei > zero.wei

  @inline def isZero: Boolean = wei == zero.wei

  @inline def toHex: String = s"0x${ wei.toString(16) }"

  override def toString: String = wei.bitLength match {
    case length if length <=3  => s"$wei Wei"
    case length if length <=6  => s"$asKWei KWei"
    case length if length <=9  => s"$asMWei MWei"
    case length if length <=12 => s"$asGWei GWei"
    case length if length <=15 => s"$asSzabo Szabo"
    case length if length <=18 => s"$asFinney Finney"
    case length if length <=21 => s"$asHucer Hucer"
    case length if length <=24 => s"$asKHucer KHucer"
    case length if length <=27 => s"$asMHucer MHucer"
    case _                     => s"$asGHucer GHucer"
  }
}
