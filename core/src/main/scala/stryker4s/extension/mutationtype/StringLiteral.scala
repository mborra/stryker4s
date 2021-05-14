package stryker4s.extension.mutationtype

import scala.meta.{Lit, Pat, Term}

case object EmptyString extends StringLiteral[Lit.String] {
  override val tree: Lit.String = Lit.String("")

  override def unapply(arg: Lit.String): Option[Lit.String] =
    super.unapply(arg).filterNot(ParentIsInterpolatedString(_))
}

case object StrykerWasHereString extends StringLiteral[Lit.String] {
  override val tree: Lit.String = Lit.String("Stryker was here!")
}

/** Not a mutation, just an extractor for pattern matching on empty string
  */
case object NonEmptyString extends NoInvalidPlacement[Lit.String] {
  override def unapply(arg: Lit.String): Option[Lit.String] =
    super
      .unapply(arg)
      .filter(_.value.nonEmpty)
      .filterNot(ParentIsInterpolatedString(_))
}

/** Not a mutation, just an extractor for pattern matching on interpolated strings
  */
case object StringInterpolation extends NoInvalidPlacement[Term.Interpolate] {
  import stryker4s.extension.TreeExtensions.IsEqualExtension

  override def unapply(arg: Term.Interpolate): Option[Term.Interpolate] =
    super.unapply(arg).filter(_.prefix.isEqual(Term.Name("s")))
}

private object ParentIsInterpolatedString {
  def apply(arg: Lit.String): Boolean =
    arg.parent match {
      // Do not mutate interpolated strings
      case Some(_: Term.Interpolate) => true
      case Some(_: Pat.Interpolate)  => true
      case _                         => false
    }
}
