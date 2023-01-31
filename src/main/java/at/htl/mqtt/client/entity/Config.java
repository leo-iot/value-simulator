package at.htl.mqtt.client.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;

@Entity
public class Config extends PanacheEntity {
    public boolean sendValues;
}
