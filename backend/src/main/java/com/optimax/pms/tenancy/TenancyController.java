package com.optimax.pms.tenancy;

import com.optimax.pms.security.UserPrincipal;
import com.optimax.pms.user.UserRole;
import com.optimax.pms.user.UserTenancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@RestController
@RequestMapping("/api/admin/tenancies")
public class TenancyController {

    private final TenancyService tenancyService;
    private final UserTenancyService userTenancyService;

    public TenancyController(TenancyService tenancyService,
                             UserTenancyService userTenancyService) {
        this.tenancyService = tenancyService;
        this.userTenancyService = userTenancyService;
    }

    @GetMapping
    public List<TenancyDto> listTenancies(@AuthenticationPrincipal UserPrincipal principal) {
        List<TenancyNode> roots;
        if (principal.getRole() == UserRole.SUPER_ADMIN) {
            // SUPER_ADMIN can see all nodes
            roots = tenancyService.findByLevel(TenancyLevel.APPLICATION);
        } else {
            roots = userTenancyService.visibleTenancyNodesForUser(principal.getId());
        }
        return toFlatSubtree(roots);
    }

    @PostMapping
    public ResponseEntity<TenancyDto> create(@RequestBody TenancyCreateRequest request) {
        if (request.level() == TenancyLevel.APPLICATION) {
            return ResponseEntity.badRequest().build();
        }

        TenancyNode parent = null;
        if (request.level() == TenancyLevel.ORGANIZATION) {
            if (request.parentId() != null) {
                parent = tenancyService.findById(request.parentId())
                        .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
            } else {
                var apps = tenancyService.findByLevel(TenancyLevel.APPLICATION);
                if (apps.isEmpty()) {
                    throw new IllegalStateException("Application root not configured");
                }
                parent = apps.get(0);
            }
        } else {
            if (request.parentId() == null) {
                throw new IllegalArgumentException("Parent is required for this level");
            }
            parent = tenancyService.findById(request.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent not found"));
        }

        TenancyNode node = TenancyNode.builder()
                .name(request.description())
                .code(request.code())
                .description(request.description())
                .level(request.level())
                .parent(parent)
                .build();
        TenancyNode saved = tenancyService.save(node);
        return ResponseEntity.ok(TenancyDto.fromEntity(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TenancyDto> update(@PathVariable Long id,
                                             @RequestBody TenancyUpdateRequest request) {
        TenancyNode existing = tenancyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tenancy not found"));
        existing.setCode(request.code());
        existing.setDescription(request.description());
        existing.setName(request.description());
        TenancyNode saved = tenancyService.save(existing);
        return ResponseEntity.ok(TenancyDto.fromEntity(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tenancyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private List<TenancyDto> toFlatSubtree(List<TenancyNode> roots) {
        List<TenancyDto> result = new ArrayList<>();
        Deque<TenancyNode> stack = new ArrayDeque<>(roots);
        while (!stack.isEmpty()) {
            TenancyNode node = stack.pop();
            result.add(TenancyDto.fromEntity(node));
            stack.addAll(node.getChildren());
        }
        return result;
    }

    public record TenancyDto(
            Long id,
            String code,
            String description,
            TenancyLevel level,
            Long parentId
    ) {
        public static TenancyDto fromEntity(TenancyNode node) {
            return new TenancyDto(
                    node.getId(),
                    node.getCode(),
                    node.getDescription(),
                    node.getLevel(),
                    node.getParent() != null ? node.getParent().getId() : null
            );
        }
    }

    public record TenancyCreateRequest(
            String code,
            String description,
            TenancyLevel level,
            Long parentId
    ) {}

    public record TenancyUpdateRequest(
            String code,
            String description
    ) {}
}

