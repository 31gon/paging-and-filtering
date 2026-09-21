package dev.triacontakaihenagon.pagingandfiltering.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class QueryCountFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(QueryCountFilter.class);
    private final Statistics stats;

    public QueryCountFilter(EntityManagerFactory emf) {
        this.stats = emf.unwrap(SessionFactory.class).getStatistics();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        long before = stats.getPrepareStatementCount();
        chain.doFilter(req, res);
        log.info("{} {}?{} -> {} SQL statements",
                req.getMethod(), req.getRequestURI(), req.getQueryString(),
                stats.getPrepareStatementCount() - before);
    }
}