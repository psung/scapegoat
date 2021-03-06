package com.sksamuel.scapegoat.inspections.exception

import com.sksamuel.scapegoat.{ Inspection, InspectionContext, Inspector, Levels }

/** @author Marconi Lanna */
class CatchException extends Inspection("Catch exception", Levels.Warning) {

  def inspector(context: InspectionContext): Inspector = new Inspector(context) {
    override def postTyperTraverser = Some apply new context.Traverser {

      import context.global._

      def catchesException(cases: List[CaseDef]) = {
        cases.exists {
          // matches t : Exception
          case CaseDef(Bind(_, Typed(_, tpt)), _, _) if tpt.tpe =:= typeOf[Exception] => true
          // matches _ : Exception
          case CaseDef(Typed(_, tpt), _, _) if tpt.tpe =:= typeOf[Exception] => true
          case _ => false
        }
      }

      override def inspect(tree: Tree): Unit = {
        tree match {
          case Try(_, cases, _) if catchesException(cases) =>
            context.warn(tree.pos, self,
              "Did you intend to catch all exceptions? Consider catching a more specific exception class: " +
                tree.toString().take(300))
          case _ => continue(tree)
        }
      }
    }
  }
}
