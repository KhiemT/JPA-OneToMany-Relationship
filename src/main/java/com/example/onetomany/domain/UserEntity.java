package com.example.onetomany.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "user_entity")
@Builder
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id","email", "firstName", "lastName"})
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Serializable {

    @Id
    @Type(type = "pg-uuid")
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    @Column(name = "user_id")
    private UUID id;

    private String email;
    private String firstName;
    private String lastName;


    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private Set<EventEntity> eventEntities = new HashSet<>();

    public void addEvent(EventEntity eventEntity) {
        if (CollectionUtils.isEmpty(eventEntities)) {
            eventEntities = new HashSet<>();
        }
        eventEntities.add(eventEntity);
        eventEntity.setUser(this);
    }

    public void removeEvent(EventEntity eventEntity) {
        eventEntities.remove(eventEntity);
        eventEntity.setUser(null);
    }

}
