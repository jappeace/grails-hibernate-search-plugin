package org.codehaus.groovy.grails.plugins.hibernate.search

import org.codehaus.groovy.grails.plugins.hibernate.search.reflection.DelegateClosure
/** commen base for HibernateSearchQueryBuilder and HibernateSearchConfig */
abstract class AbstractHibernateSearch{

	def invokeClosureNode( Closure callable ) {
		new DelegateClosure(to:this).invoke(callable)
	}


}

