package com.github.hannesknutsson.hungryboii.structure.dataclasses;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "discorduser")
public class DiscordUser {

    @Id
    long id;

}
