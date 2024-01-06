package abistech.resseract.data.source.impl;

public class PostGresDBSource extends AbstractDBSource {
    @Override
    String getDBDriverName() {
        return "org.postgresql.Driver";
    }

    @Override
    String getUrl(String host, String port, String dbName) {
        return String.format("jdbc:postgresql://%s:%s/%s", host, port, dbName);
    }

    @Override
    String getDefaultPort() {
        return "5432";
    }
}
