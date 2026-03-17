package com.optimax.pms.user;

import com.optimax.pms.permissions.Permission;
import com.optimax.pms.tenancy.TenancyLevel;
import com.optimax.pms.tenancy.TenancyNode;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_tenancy_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTenancyAssignment {

    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenancy_id", insertable = false, updatable = false)
    private TenancyNode tenancyNode;

    @Enumerated(EnumType.STRING)
    @Column(name = "tenancy_level", nullable = false, length = 32)
    private TenancyLevel level;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_tenancy_permissions", joinColumns = {
            @JoinColumn(name = "user_id"),
            @JoinColumn(name = "tenancy_id")
    })
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false, length = 64)
    private Set<Permission> permissions = new HashSet<>();

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "user_id")
        private Long userId;

        @Column(name = "tenancy_id")
        private Long tenancyId;
    }
}

