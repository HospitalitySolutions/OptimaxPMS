package com.optimax.pms.user;

import com.optimax.pms.permissions.Permission;
import com.optimax.pms.tenancy.TenancyLevel;
import com.optimax.pms.tenancy.TenancyNode;
import com.optimax.pms.tenancy.TenancyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserTenancyService {

    private final UserTenancyAssignmentRepository assignmentRepository;
    private final TenancyService tenancyService;

    public UserTenancyService(UserTenancyAssignmentRepository assignmentRepository,
                              TenancyService tenancyService) {
        this.assignmentRepository = assignmentRepository;
        this.tenancyService = tenancyService;
    }

    public List<UserTenancyAssignment> findAssignmentsForUser(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }

    /**
     * Resolve effective permissions for a user in the context of a given tenancy node.
     * This walks up the tenancy tree, aggregating permissions from ancestor assignments.
     */
    public Set<Permission> resolveEffectivePermissions(Long userId, Long tenancyNodeId) {
        Set<Permission> result = new HashSet<>();

        Optional<TenancyNode> currentOpt = tenancyService.findById(tenancyNodeId);
        while (currentOpt.isPresent()) {
            TenancyNode current = currentOpt.get();
            assignmentRepository.findByIdUserIdAndIdTenancyId(userId, current.getId())
                    .ifPresent(assignment -> result.addAll(assignment.getPermissions()));
            currentOpt = Optional.ofNullable(current.getParent());
        }
        return result;
    }

    public boolean hasPermission(Long userId, Long tenancyNodeId, Permission permission) {
        return resolveEffectivePermissions(userId, tenancyNodeId).contains(permission);
    }

    public List<TenancyNode> visibleTenancyNodesForUser(Long userId) {
        List<UserTenancyAssignment> assignments = assignmentRepository.findByUserId(userId);
        return assignments.stream()
                .map(UserTenancyAssignment::getTenancyNode)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserTenancyAssignment assignUserToTenancy(User user,
                                                     TenancyNode tenancyNode,
                                                     TenancyLevel level,
                                                     Set<Permission> permissions) {
        UserTenancyAssignment.Id id = new UserTenancyAssignment.Id(user.getId(), tenancyNode.getId());
        UserTenancyAssignment assignment = UserTenancyAssignment.builder()
                .id(id)
                .user(user)
                .tenancyNode(tenancyNode)
                .level(level)
                .permissions(permissions != null ? permissions : new HashSet<>())
                .build();
        return assignmentRepository.save(assignment);
    }
}

