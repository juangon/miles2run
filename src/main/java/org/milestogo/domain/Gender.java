package org.milestogo.domain;

/**
 * Created by shekhargulati on 07/03/14.
 */
public enum Gender {
    MALE("male"), FEMALE("female");

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public static Gender fromStringToGender(String genderStr) {
        Gender[] values = Gender.values();
        for (Gender gender : values) {
            if (gender.gender.equals(genderStr)) {
                return gender;
            }
        }
        return null;
    }

    public String getGender() {
        return gender;
    }
}
