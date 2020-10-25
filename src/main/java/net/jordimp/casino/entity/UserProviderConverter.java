package net.jordimp.casino.entity;

import javax.persistence.AttributeConverter;
import java.util.stream.Stream;

public class UserProviderConverter implements AttributeConverter<UserProvider, String> {
 
    @Override
    public String convertToDatabaseColumn(UserProvider userProvider) {
        if (userProvider == null) {
            return null;
        }
        return userProvider.getName();
    }
 
    @Override
    public UserProvider convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }
 
        return Stream.of(UserProvider.values())
          .filter(c -> c.getName().equals(name))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }

}
