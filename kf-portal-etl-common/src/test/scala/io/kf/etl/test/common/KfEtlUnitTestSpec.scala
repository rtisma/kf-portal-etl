package io.kf.etl.test.common

import io.kf.etl.common.url.KfURLEnabler
import org.scalatest._

abstract class KfEtlUnitTestSpec extends FlatSpec with Matchers with
  OptionValues with Inside with Inspectors with KfURLEnabler
