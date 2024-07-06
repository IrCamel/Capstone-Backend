package com.progetto.personale.capstone.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDTO {
    private int followersCount;
    private int followingCount;
    private boolean isFollowing;
}
