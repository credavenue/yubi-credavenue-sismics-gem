package com.sismics.docs.core.util.jpa;

import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.sismics.util.context.ThreadLocalContext;
import org.hibernate.query.internal.NativeQueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query utilities.
 *
 * @author jtremeaux 
 */
public class QueryUtil {
    private static Logger log = LoggerFactory.getLogger(QueryUtil.class);

    /**
     * Creates a native query from the query parameters.
     * 
     * @param queryParam Query parameters
     * @return Native query
     */
    public static Query getNativeQuery(QueryParam queryParam) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createNativeQuery(queryParam.getQueryString());
        for (Entry<String, Object> entry : queryParam.getParameterMap().entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if(query instanceof NativeQueryImpl)
        log.info("QueryUtil::getNativeQuery =>"+ ((NativeQueryImpl<?>) query).getQueryString());
        return query;
    }
    
    /**
     * Returns sorted query parameters.
     * 
     * @param queryParam Query parameters
     * @param sortCriteria Sort criteria
     * @return Sorted query parameters
     */
    public static QueryParam getSortedQueryParam(QueryParam queryParam, SortCriteria sortCriteria) {
        StringBuilder sb = new StringBuilder(queryParam.getQueryString());
        if (sortCriteria != null) {
            sb.append(" order by c");
            sb.append(sortCriteria.getColumn());
            sb.append(sortCriteria.isAsc() ? " asc" : " desc");
        }
        
        return new QueryParam(sb.toString(), queryParam.getParameterMap());
    }
}
