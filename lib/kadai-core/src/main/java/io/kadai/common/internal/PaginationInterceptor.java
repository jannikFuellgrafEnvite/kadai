package io.kadai.common.internal;

import java.sql.Connection;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

@Intercepts({
  @Signature(
      type = StatementHandler.class,
      method = "prepare",
      args = {Connection.class, Integer.class})
})
public class PaginationInterceptor implements Interceptor {
  private static final ThreadLocal<Pagination> PAGINATION_THREAD_LOCAL = new ThreadLocal<>();

  public static void setPagination(int offset, int limit) {
    PAGINATION_THREAD_LOCAL.set(new Pagination(offset, limit));
  }

  public static void clearPagination() {
    PAGINATION_THREAD_LOCAL.remove();
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    if (PAGINATION_THREAD_LOCAL.get() == null) {
      return invocation.proceed();
    }

    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

    while (metaObject.hasGetter("delegate")) {
      statementHandler = (StatementHandler) metaObject.getValue("delegate");
      metaObject = SystemMetaObject.forObject(statementHandler);
    }

    BoundSql boundSql = statementHandler.getBoundSql();
    String originalSql = boundSql.getSql();

    if (!metaObject.hasGetter("mappedStatement")) {
      throw new IllegalStateException("DatabaseId could not be determined.");
    }
    MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");

    String databaseId = mappedStatement.getConfiguration().getDatabaseId();

    Pagination pagination = PAGINATION_THREAD_LOCAL.get();
    String paginatedSql;

    if ("db2".equalsIgnoreCase(databaseId)) {
      paginatedSql =
          originalSql
              + " OFFSET "
              + pagination.offset
              + " ROWS"
              + " FETCH FIRST "
              + pagination.limit
              + " ROWS ONLY";
    } else {
      paginatedSql = originalSql + " LIMIT " + pagination.limit + " OFFSET " + pagination.offset;
    }

    BoundSql newBoundSql =
        new BoundSql(
            mappedStatement.getConfiguration(),
            paginatedSql,
            boundSql.getParameterMappings(),
            boundSql.getParameterObject());

    metaObject.setValue("boundSql", newBoundSql);

    clearPagination();
    return invocation.proceed();
  }

  private static class Pagination {
    private final int offset;
    private final int limit;

    public Pagination(int offset, int limit) {
      this.offset = offset;
      this.limit = limit;
    }

    public int getOffset() {
      return offset;
    }

    public int getLimit() {
      return limit;
    }
  }
}
