package com.optimax.pms.tenancy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TenancyService {

    private final TenancyRepository tenancyRepository;

    public TenancyService(TenancyRepository tenancyRepository) {
        this.tenancyRepository = tenancyRepository;
    }

    public Optional<TenancyNode> findById(Long id) {
        return tenancyRepository.findById(id);
    }

    public List<TenancyNode> findChildren(Long parentId) {
        return tenancyRepository.findByParentId(parentId);
    }

    public List<TenancyNode> findByLevel(TenancyLevel level) {
        return tenancyRepository.findByLevel(level);
    }

    @Transactional
    public TenancyNode save(TenancyNode node) {
        return tenancyRepository.save(node);
    }

    @Transactional
    public void delete(Long id) {
        tenancyRepository.deleteById(id);
    }
}

