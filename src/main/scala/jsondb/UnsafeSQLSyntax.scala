package scalikejdbc 

//This is an override to allow sql statement to be created from variables

import scalikejdbc.interpolation.SQLSyntax 

object UnsafeSQLSyntax { 
  def apply(s: String, p: Seq[Any]): SQLSyntax = SQLSyntax(s, p) 
} 
