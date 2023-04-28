package ge.artkme.webapps.api;

import ge.artkme.webapps.exception.DBManagerException;
import oracle.jdbc.OracleConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class DBManager implements AutoCloseable {
    public static final String CONTEXT_NAME = "java:comp/env";
    public static final String DS_NAME = "jdbc/movieWs";

//    private static final Logger lgg = LogManager.getLogger(DBManager.class);
    protected Connection conn;
    protected OracleConnection oraConn;
    protected final Map<SqlStatement, CallableStatement> statementPool;

    protected interface SqlStatement {
        String getSql();
    }

    public DBManager() throws DBManagerException {
        this.conn = getConnection();
        this.oraConn = getOracleConnection();
        this.statementPool = new HashMap<>();
    }

    private Connection getConnection() throws DBManagerException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup(CONTEXT_NAME);
            DataSource ds = (DataSource) envContext.lookup(DS_NAME);
            return ds.getConnection();
        } catch (NamingException e) {
            throw new DBManagerException("Could not lookup data source", e);
        } catch (SQLException e) {
            throw new DBManagerException("Could not connect to database", e);
        }
    }

    private OracleConnection getOracleConnection() throws DBManagerException {
        try {
            return conn.unwrap(OracleConnection.class);
        } catch (SQLException e) {
            throw new DBManagerException("Could not unwrap Connection as OracleConnection", e);
        }
    }

    protected CallableStatement getStatement(SqlStatement statement)
            throws SQLException {
        CallableStatement cstmt = statementPool.get(statement);
        if (cstmt == null) {
            cstmt = conn.prepareCall(statement.getSql());
            statementPool.put(statement, cstmt);
        }
        return cstmt;
    }

    public static void closeResultSetIfRequired(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
//                lgg.warn("Unable to close result set ", e);
            }
        }
    }

    private void tryCloseCallableStatement(CallableStatement cstmt) {
        try {
            if (cstmt != null)
                cstmt.close();
        } catch (SQLException e) {
//            lgg.warn(
//                    "Error during finalization, could not close CallableStatement",
//                    e);
        }
    }

    private void tryCloseOracleConnection() {
        try {
            if (oraConn != null && !oraConn.isClosed()) {
                oraConn.close();
            }
        } catch (SQLException e) {
//            lgg.warn("Error during finalization, could not close OracleConnection", e);
        }
    }

    private void tryCloseConnection() {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
//            lgg.warn("Error during finalization, could not close Connection", e);
        }
    }

    @Override
    public void close() {
        Set<SqlStatement> statementKeys = statementPool.keySet();
        for (SqlStatement k : statementKeys) {
            tryCloseCallableStatement(statementPool.get(k));
        }
        statementPool.clear();
        tryCloseConnection();
        tryCloseOracleConnection();
    }

}
