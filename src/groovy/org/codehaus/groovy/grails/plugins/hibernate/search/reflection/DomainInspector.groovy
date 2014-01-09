package org.codehaus.groovy.grails.plugins.hibernate.search.reflection

import org.codehaus.groovy.grails.commons.GrailsApplication 
/** holds logic to inspect classes to allow manipulation of them */
class DomainInspector{
	GrailsApplication app	

	def withIndexedDomainClasses(Closure callable){

		app.domainClasses.each { grailsClass ->

			def clazz = grailsClass.clazz

			if (
				ClassPropertyFetcher.forClass( clazz ).getStaticPropertyValue( "search", Closure ) ||
				AnnotationUtils.isAnnotationDeclaredLocally( Indexed, clazz )
			){
				callable(grailsClass)					
			}
		}
	}
		

}
