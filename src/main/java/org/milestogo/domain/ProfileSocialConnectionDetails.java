package org.milestogo.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shekhargulati on 12/03/14.
 */
public class ProfileSocialConnectionDetails {
    private Long id;
    private String username;
    private final List<String> providers = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getProviders() {
        return providers;
    }
}
