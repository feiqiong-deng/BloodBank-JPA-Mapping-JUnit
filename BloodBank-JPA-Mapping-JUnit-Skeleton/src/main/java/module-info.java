@SuppressWarnings("all")
open module bloodbank {
	requires java.sql;
	requires java.instrument;
	requires org.apache.logging.log4j;
	requires java.persistence;
	requires java.annotation;
}