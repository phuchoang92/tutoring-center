package com.phuc.tutoring_center.core.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceStatus {
    PRESENT("Present", true),
    ABSENT("Absent", false),
    LATE("Late", true),
    EXCUSED("Excused", true);

    private final String displayName;
    private final boolean isCountedAsPresent;

    public static AttendanceStatus fromString(String status) {
        try {
            return valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid attendance status: " + status);
        }
    }

    public boolean isCountedAsPresent() {
        return isCountedAsPresent;
    }

    @Override
    public String toString() {
        return displayName;
    }
} 