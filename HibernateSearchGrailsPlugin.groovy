import org.codehaus.groovy.grails.plugins.hibernate.search.HibernateSearchConfig
import org.codehaus.groovy.grails.plugins.hibernate.search.HibernateSearchQueryBuilder
import org.codehaus.groovy.grails.plugins.hibernate.search.SearchMappingConfigurableLocalSessionFactoryBean
import org.codehaus.groovy.grails.plugins.hibernate.search.reflection.DomainInspector
import org.hibernate.*
import org.hibernate.search.Search

class HibernateSearchGrailsPlugin {
	def version = "0.81"
	def grailsVersion = "2.3.3"
	def loadAfter = ['hibernate']
	def title = "Hibernate Search Plugin"
	def author = "Mathieu Perez, Julie Ingignoli"
	def authorEmail = "mathieu.perez@novacodex.net, julie.ingignoli@novacodex.net"
	def description = 'Integrates Hibernate Search features to Grails'
	def documentation = "http://grails.org/plugin/hibernate-search"
	def license = 'APACHE'
	def organization = [name: 'NovaCodex', url: 'http://www.novacodex.net/']
	def developers = [[name: 'Mathieu Perez', email: 'mathieu.perez@novacodex.net'],
			[name: 'Julie Ingignoli', email: 'julie.ingignoli@novacodex.net'],
				[name: 'Jappie Klooster', email: 'jappieklooster@hotmail.com']]
	def issueManagement = [system: 'github', url: 'https://github.com/mathpere/grails-hibernate-search-plugin/issues']
	def scm = [url: 'https://github.com/mathpere/grails-hibernate-search-plugin']

	def doWithSpring = {
		sessionFactory( SearchMappingConfigurableLocalSessionFactoryBean ) { bean ->
			// see org.codehaus.groovy.grails.plugins.orm.hibernate.HibernatePluginSupport:
			bean.parent = 'abstractSessionFactoryBeanConfig'
		}
	}

	def doWithDynamicMethods = { ctx ->

		// get the hibernate session (this used to be a pain in hibernate 3, which caused some confusing code)
		Session hiberTextSes = Search.getFullTextSession(
				ctx.sessionFactory.currentSession
		) 

		new DomainInspector(app: application).withIndexedDomainClasses{ grailsClass ->
			def clazz = grailsClass.clazz
			// add search() method to indexed domain classes:
			grailsClass.metaClass.static.search = { 
				new HibernateSearchQueryBuilder(clazz, hiberTextSes)
			}
			
			// add search() method to indexed domain instances:
			grailsClass.metaClass.search = {
				new HibernateSearchQueryBuilder(clazz, delegate, hiberTextSes)
			}
		}

		// load config and execute
		new HibernateSearchConfig(hiberTextSes).invokeClosureNode(
			application.config.grails.plugins.hibernatesearch
		)

	}
}
