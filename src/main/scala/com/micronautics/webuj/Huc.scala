package com.micronautics.webuj

object Huc {
  lazy val zero: Huc = Huc(0)

  /** @return a BigInt containing 1 with the given number of zeros after it, e.g. {{{e(3) == BigInt(1000)}}} */
  @inline def e(howManyZeros: Int): BigInt = BigInt(s"1${ zeros(howManyZeros) }")

  /** @return a String containing the specified number of zeros, e.g. {{{zeros(3) == "000"}}} */
  @inline def zeros(howMany: Int): String = "0" * howMany

  /** @param value unit is wei */
  @inline def apply(value: String):          Huc = new Huc(BigInt(value))
  @inline def apply(value: BigInt):          Huc = new Huc(value)
  @inline def apply(value: Int):             Huc = new Huc(BigInt(value))
  @inline def apply(value: Double):          Huc = new Huc(BigDecimal(value).bigDecimal.toBigInteger)
  @inline def apply(value: BigDecimal):      Huc = new Huc(value.bigDecimal.toBigInteger)

  @inline def fromWei(value: String):        Huc = Huc(value)
  @inline def fromWei(value: Double):        Huc = Huc(value)
  @inline def fromWei(value: BigDecimal):    Huc = Huc(value)
  @inline def fromWei(value: Int):           Huc = Huc(value)
  @inline def fromWei(value: BigInt):        Huc = Huc(value)

  @inline def fromKWei(value: String):       Huc = Huc(value + zeros(3))
  @inline def fromKWei(value: Double):       Huc = Huc(BigDecimal(value) * BigDecimal(e(3)))
  @inline def fromKWei(value: BigDecimal):   Huc = Huc(value * BigDecimal(e(3)))
  @inline def fromKWei(value: Int):          Huc = Huc(value * e(3))
  @inline def fromKWei(value: BigInt):       Huc = Huc(value * e(3))

  @inline def fromMWei(value: String):       Huc = Huc(value + zeros(6))
  @inline def fromMWei(value: Double):       Huc = Huc(BigDecimal(value) * BigDecimal(e(6)))
  @inline def fromMWei(value: BigDecimal):   Huc = Huc(value * BigDecimal(e(6)))
  @inline def fromMWei(value: Int):          Huc = Huc(value * e(6))
  @inline def fromMWei(value: BigInt):       Huc = Huc(value * e(6))

  @inline def fromGWei(value: String):       Huc = Huc(value + zeros(9))
  @inline def fromGWei(value: Double):       Huc = Huc(BigDecimal(value) * BigDecimal(e(9)))
  @inline def fromGWei(value: BigDecimal):   Huc = Huc(value * BigDecimal(e(9)))
  @inline def fromGWei(value: Int):          Huc = Huc(value * e(9))
  @inline def fromGWei(value: BigInt):       Huc = Huc(value * e(9))

  @inline def fromTWei(value: String):      Huc = Huc(value + zeros(12))
  @inline def fromTWei(value: Double):      Huc = Huc(BigDecimal(value) * BigDecimal(e(12)))
  @inline def fromTWei(value: BigDecimal):  Huc = Huc(value * BigDecimal(e(12)))
  @inline def fromTWei(value: Int):         Huc = Huc(value * e(12))
  @inline def fromTWei(value: BigInt):      Huc = Huc(value * e(12))

  @inline def fromFinney(value: String):     Huc = Huc(value + zeros(15))
  @inline def fromFinney(value: Double):     Huc = Huc(BigDecimal(value) * BigDecimal(e(15)))
  @inline def fromFinney(value: BigDecimal): Huc = Huc(value * BigDecimal(e(15)))
  @inline def fromFinney(value: Int):        Huc = Huc(value * e(15))
  @inline def fromFinney(value: BigInt):     Huc = Huc(value * e(15))

  @inline def fromHuc(value: String):      Huc = Huc(value + zeros(18))
  @inline def fromHuc(value: Double):      Huc = Huc(BigDecimal(value) * BigDecimal(e(18)))
  @inline def fromHuc(value: BigDecimal):  Huc = Huc(value * BigDecimal(e(18)))
  @inline def fromHuc(value: Int):         Huc = Huc(value * e(18))
  @inline def fromHuc(value: BigInt):      Huc = Huc(value * e(18))

  @inline def fromKHuc(value: String):     Huc = Huc(value + zeros(21))
  @inline def fromKHuc(value: Double):     Huc = Huc(BigDecimal(value) * BigDecimal(e(21)))
  @inline def fromKHuc(value: BigDecimal): Huc = Huc(value * BigDecimal(e(21)))
  @inline def fromKHuc(value: Int):        Huc = Huc(value * e(21))
  @inline def fromKHuc(value: BigInt):     Huc = Huc(value * e(21))

  @inline def fromMHuc(value: String):     Huc = Huc(value + zeros(24))
  @inline def fromMHuc(value: Double):     Huc = Huc(BigDecimal(value) * BigDecimal(e(24)))
  @inline def fromMHuc(value: BigDecimal): Huc = Huc(value * BigDecimal(e(24)))
  @inline def fromMHuc(value: Int):        Huc = Huc(value * e(24))
  @inline def fromMHuc(value: BigInt):     Huc = Huc(value * e(24))

  @inline def fromGHuc(value: String):     Huc = Huc(value + zeros(27))
  @inline def fromGHuc(value: Double):     Huc = Huc(BigDecimal(value) * BigDecimal(e(27)))
  @inline def fromGHuc(value: BigDecimal): Huc = Huc(value * BigDecimal(e(27)))
  @inline def fromGHuc(value: Int):        Huc = Huc(value * e(27))
  @inline def fromGHuc(value: BigInt):     Huc = Huc(value * e(27))
}

/** Wei are the smallest unit of currency and are always integers, never fractional quantities */
class Huc(val wei: BigInt) extends Ordered[Huc] {
  import Huc._

  @inline def *(value: Huc): Huc      = Huc(wei * value.wei)
  @inline def *(value: Int): Huc        = Huc(wei * value)
  @inline def *(value: Double): Huc     = Huc(BigDecimal(wei) * value)
  @inline def *(value: BigInt): Huc     = Huc(value) * wei
  @inline def *(value: BigDecimal): Huc = Huc(value) * wei

  @inline def /(value: Huc): Huc      = Huc(wei / value.wei)
  @inline def /(value: Int): Huc        = Huc(wei / value)
  @inline def /(value: Double): Huc     = Huc(BigDecimal(wei) / value)
  @inline def /(value: BigInt): Huc     = Huc(value) / wei
  @inline def /(value: BigDecimal): Huc = Huc(value) / wei

  @inline def +(value: Huc): Huc      = Huc(wei + value.wei)
  @inline def +(value: Int): Huc        = Huc(wei + value)
  @inline def +(value: Double): Huc     = Huc(BigDecimal(wei) + value)
  @inline def +(value: BigInt): Huc     = Huc(value) + wei
  @inline def +(value: BigDecimal): Huc = Huc(value) + wei

  @inline def -(value: Huc): Huc      = Huc(wei - value.wei)
  @inline def -(value: Int): Huc        = Huc(wei - value)
  @inline def -(value: Double): Huc     = Huc(BigDecimal(wei) - value)
  @inline def -(value: BigInt): Huc     = Huc(value) - wei
  @inline def -(value: BigDecimal): Huc = Huc(value) - wei

  @inline def bigInteger: java.math.BigInteger = wei.bigInteger
  @inline def asWei: BigInt        = wei
  @inline def asKWei: BigDecimal   = bigDecimal(wei / e(3))
  @inline def asMWei: BigDecimal   = bigDecimal(wei / e(6))
  @inline def asGWei: BigDecimal   = bigDecimal(wei / e(9))
  @inline def asTWei: BigDecimal  = bigDecimal(wei / e(12))
  @inline def asFinney: BigDecimal = bigDecimal(wei / e(15))
  @inline def asHuc: BigDecimal  = bigDecimal(wei / e(18))
  @inline def asKHuc: BigDecimal = bigDecimal(wei / e(21))
  @inline def asMHuc: BigDecimal = bigDecimal(wei / e(24))
  @inline def asGHuc: BigDecimal = bigDecimal(wei / e(27))

  /** @return Amount of Huc corresponding to the given wei value */
  @inline def bigDecimal(wei: BigInt): java.math.BigDecimal =
    new java.math.BigDecimal(wei.bigInteger)
      .setScale(16, java.math.BigDecimal.ROUND_DOWN)

  @inline def compare(that: Huc): Int = this.wei compare that.wei

  @inline override def equals(that: Any): Boolean =
    that match {
      case that: Huc =>
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
    case length if length <=15 => s"$asTWei TWei"
    case length if length <=18 => s"$asFinney Finney"
    case length if length <=21 => s"$asHuc Huc"
    case length if length <=24 => s"$asKHuc KHuc"
    case length if length <=27 => s"$asMHuc MHuc"
    case _                     => s"$asGHuc GHuc"
  }
}
