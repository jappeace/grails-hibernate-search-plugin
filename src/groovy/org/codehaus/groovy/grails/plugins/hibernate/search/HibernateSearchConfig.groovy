package org.codehaus.groovy.grails.plugins.hibernate.search

import org.hibernate.Session
import org.hibernate.search.FullTextSession
import org.hibernate.search.MassIndexer
import org.hibernate.search.Search
import org.apache.commons.logging.LogFactory

class HibernateSearchConfig extends AbstractHibernateSearch{


	protected final static log = LogFactory.getLog this

	private MassIndexer massIndexer
	private final FullTextSession fullTextSession
	private static final List MASS_INDEXER_METHODS = MassIndexer.methods.findAll { it.returnType == MassIndexer }*.name

	HibernateSearchConfig( Session session ) {
		this.fullTextSession = Search.getFullTextSession( session )
	}

	/**
	 *
	 * Rebuild the indexes of all indexed entity types with custom config
	 *
	 */
	def rebuildIndexOnStart( Closure massIndexerDsl ) {

		if ( log.debugEnabled ){
			log.debug "Start rebuilding indexes of all indexed entity types..."
		}
		massIndexer = fullTextSession.createIndexer()
		invokeClosureNode massIndexerDsl
		massIndexer.startAndWait()
	}

	/**
	 *
	 * Rebuild the indexes of all indexed entity types with default options:
	 * - CacheMode.IGNORE
	 * - purgeAllOnStart = true
	 * - optimizeAfterPurge = true
	 * - optimizeOnFinish = true
	 *
	 */
	def rebuildIndexOnStart( boolean rebuild ) {

		if ( !rebuild ){
			return
		}
		if ( log.debugEnabled ){
			log.debug "Start rebuilding indexes of all indexed entity types..."
		}
		massIndexer = fullTextSession.createIndexer().startAndWait()
	}
	/** makes it possible to ignore not concerned config */
	Object invokeMethod( String name, Object args ) {
		if ( name in MASS_INDEXER_METHODS ) {
			massIndexer = massIndexer.invokeMethod name, args
		}else{
			log.info "ignoring config: " + name
		}

	}
}
