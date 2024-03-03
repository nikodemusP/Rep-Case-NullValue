package com.example.starter;

import java.sql.JDBCType;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.jdbcclient.SqlOutParam;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.data.NullValue;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    final String dbNode="### TBD ####";
    final String user="### TBD ####";
    final String pwd="### TBD ####";
    
    final JsonObject config = new JsonObject()
                    .put("url", "jdbc:oracle:thin:@//"+dbNode+":1521/node1instance")
                    .put("provider_class","com.example.starter.HikariCPConnectionPool")
                    .put("driverClassName","oracle.jdbc.OracleDriver")
                    .put("datasourceName", "pool-name")
                    .put("user", user)
                    .put("password", pwd)
                    .put("max_pool_size", 5);

    JDBCPool pool = JDBCPool.pool(vertx, config);


    vertx.createHttpServer().requestHandler(req -> {
      pool
        .preparedQuery("call test_inout.test_1_inout(test_1 => ?);")
        .execute(Tuple.of(SqlOutParam.INOUT(NullValue.Double,JDBCType.DOUBLE)))
        .onFailure(e -> {
          e.printStackTrace();
          try {
            System.out.println("In: "  + JDBCType.valueOf(-9).getName());
          } catch (Exception x) {
            x.printStackTrace();;
          }
          try {
            System.out.println("Out: " + JDBCType.valueOf(-6).getName());  
          } catch (Exception x) {
            x.printStackTrace();;
          }
        })
        .onSuccess(rows -> {
          for (Row row : rows) {
            System.out.println(row.getValue(0));
          }
          req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!");
  
        })
        .onFailure(err -> { err.printStackTrace(); });

    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
