package bloodbank.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2022-03-21T16:24:02.062-0400")
@StaticMetamodel(PojoBaseCompositeKey.class)
public class PojoBaseCompositeKey_ {
	public static volatile SingularAttribute<PojoBaseCompositeKey, Serializable> id;
	public static volatile SingularAttribute<PojoBaseCompositeKey, Integer> version;
	public static volatile SingularAttribute<PojoBaseCompositeKey, LocalDateTime> created;
	public static volatile SingularAttribute<PojoBaseCompositeKey, LocalDateTime> updated;
}
