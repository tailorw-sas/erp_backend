package com.kynsoft.finamer.audit.infrastructure.bus.query;

import com.kynsoft.finamer.audit.domain.bus.query.IQuery;
import com.kynsoft.finamer.audit.domain.bus.query.IQueryBus;
import com.kynsoft.finamer.audit.domain.bus.query.IQueryHandler;
import com.kynsoft.finamer.audit.domain.bus.query.IResponse;
import com.kynsoft.finamer.audit.domain.bus.query.QueryHandlerExecutionError;
import com.kynsoft.finamer.audit.domain.bus.query.QueryNotRegisteredError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@SuppressWarnings("all")
@Service
public final class InMemoryQueryBus implements IQueryBus {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryQueryBus.class);

    private final QueryHandlersInformation information;

    private final ApplicationContext context;

    public InMemoryQueryBus(QueryHandlersInformation information, ApplicationContext context) {
        this.information = information;
        this.context = context;
    }

    @Override
    public IResponse ask(IQuery query) throws QueryHandlerExecutionError {
        try {
            Class<? extends IQueryHandler> queryHandlerClass = information.search(query.getClass());
            IQueryHandler handler = context.getBean(queryHandlerClass);
            return handler.handle(query);
        } catch (QueryNotRegisteredError ex) {
            throw new QueryHandlerExecutionError(ex);
        } catch (Exception exception) {
            logger.info("Error executing query: ", exception);
            throw exception;
        }
    }
}
