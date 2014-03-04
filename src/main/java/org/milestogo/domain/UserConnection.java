package org.milestogo.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by shekhargulati on 04/03/14.
 */
@Entity
public class UserConnection implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String accessToken;
    @NotNull
    private String accessSecret;
    @NotNull
    private String socialNetwork;
    @NotNull
    private String socialNetworkHandle;

    @NotNull
    private String socialNetworkId;

    public UserConnection() {
    }

    public UserConnection(String accessToken, String accessSecret, String socialNetwork, String socialNetworkHandle,  String socialNetworkId) {
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.socialNetwork = socialNetwork;
        this.socialNetworkHandle = socialNetworkHandle;
        this.socialNetworkId = socialNetworkId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(String socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public String getSocialNetworkHandle() {
        return socialNetworkHandle;
    }

    public void setSocialNetworkHandle(String socialNetworkHandle) {
        this.socialNetworkHandle = socialNetworkHandle;
    }

    public String getSocialNetworkId() {
        return socialNetworkId;
    }

    public void setSocialNetworkId(String socialNetworkId) {
        this.socialNetworkId = socialNetworkId;
    }
}
