package com.sns.sns.service.domain.member.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sns.sns.service.common.BaseTimeEntity;
import com.sns.sns.service.domain.board.model.BoardEntity;
import com.sns.sns.service.domain.comment.model.CommentEntity;
import com.sns.sns.service.domain.favorite.model.FavoriteEntity;
import com.sns.sns.service.domain.file.model.ImageEntity;
import com.sns.sns.service.domain.member.model.UserRole;
import com.sns.sns.service.domain.member.model.UserStatus;
import com.sns.sns.service.domain.notification.model.NotificationEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member extends BaseTimeEntity implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userLoginId;
    private String userName;
    private String userEmail;
    private String password;
    private String userProfileImgUrl;
    private long visitedTimes;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteEntity> favoriteEntityList = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotificationEntity> notificationEntityList = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImageEntity> imageEntities = new ArrayList<>();
    @JsonIgnore
    public Member(
            String userLoginId,
            String username,
            String password,
            String userEmail,
            String userProfileImgUrl,
            UserStatus userStatus
    ){
        this.userLoginId = userLoginId;
        this.userName = username;
        this.password = password;
        this.userEmail = userEmail;
        this.userProfileImgUrl = userProfileImgUrl;
        this.userStatus = userStatus;
    }
    @JsonIgnore
    public Member UpdateMemberInfo(
            String userEmail,
            String password,
            String userProfileImgUrl
    ){
        this.userEmail = userEmail;
        this.password = password;
        this.userProfileImgUrl = userProfileImgUrl;
        return this;
    }

    @JsonIgnore
    public void UpdateMemberStatus(
            UserStatus userStatus
    ){
        this.userStatus = userStatus;
    }

    public void UpdateVisitedCount(){
        this.visitedTimes +=1;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.getUserLoginId();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
