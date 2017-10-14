package Entities.CreationUnit;

import Entities.PersonalResearchEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2015-10-28T22:43:10")
@StaticMetamodel(PersonalResearchEntity.class)
public class PersonalResearchEntity_ { 

    public static volatile SingularAttribute<PersonalResearchEntity, Boolean> deleted;
    public static volatile SingularAttribute<PersonalResearchEntity, Boolean> voted;
    public static volatile SingularAttribute<PersonalResearchEntity, String> category;
    public static volatile SingularAttribute<PersonalResearchEntity, String> userid;
    public static volatile SingularAttribute<PersonalResearchEntity, String> researchid;
    public static volatile SingularAttribute<PersonalResearchEntity, Integer> transactionid;

}