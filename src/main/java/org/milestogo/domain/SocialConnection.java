package org.milestogo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
public class SocialConnection implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String accessToken;

    @NotNull
    private String accessSecret;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialProvider provider;

    @NotNull
    private String handle;

    @NotNull
    private String connectionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private final Date connectedOn = new Date();

    @ManyToOne
    private Profile profile;

    public SocialConnection() {
    }

    public SocialConnection(String accessToken, String accessSecret, SocialProvider provider, String handle, String connectionId) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.provider = provider;
        this.handle = handle;
        this.connectionId = connectionId;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public SocialProvider getProvider() {
        return provider;
    }

    public void setProvider(SocialProvider provider) {
        this.provider = provider;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public Date getConnectedOn() {
        return connectedOn;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "SocialConnection{" +
                "id=" + id +
                ", accessToken='" + accessToken + '\'' +
                ", accessSecret='" + accessSecret + '\'' +
                ", provider=" + provider +
                ", handle='" + handle + '\'' +
                ", connectionId='" + connectionId + '\'' +
                ", connectedOn=" + connectedOn +
                ", profile=" + profile +
                '}';
    }
}
