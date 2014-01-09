package org.codehaus.groovy.grails.plugins.hibernate.search.reflection

/** lets a closure be executed as if it where a member method of the given object */
class DelegateClosure{

	def to
	
	def invoke( Closure callable ) {
		if ( !callable ){
			return
		}

		callable.delegate = to
		callable.resolveStrategy = Closure.DELEGATE_FIRST
		callable.call()
	}

}
