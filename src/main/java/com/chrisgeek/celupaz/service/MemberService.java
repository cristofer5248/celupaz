package com.chrisgeek.celupaz.service;

import com.chrisgeek.celupaz.domain.Member;
import com.chrisgeek.celupaz.repository.MemberRepository;
import com.chrisgeek.celupaz.security.AuthoritiesConstants;
import com.chrisgeek.celupaz.security.SecurityUtils;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.chrisgeek.celupaz.domain.Member}.
 */
@Service
@Transactional
public class MemberService {

    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * Save a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member save(Member member) {
        LOG.debug("Request to save Member : {}", member);
        return memberRepository.save(member);
    }

    /**
     * Update a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member update(Member member) {
        LOG.debug("Request to update Member : {}", member);
        return memberRepository.save(member);
    }

    /**
     * Partially update a member.
     *
     * @param member the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Member> partialUpdate(Member member) {
        LOG.debug("Request to partially update Member : {}", member);

        return memberRepository
            .findById(member.getId())
            .map(existingMember -> {
                updateIfPresent(existingMember::setName, member.getName());
                updateIfPresent(existingMember::setEmail, member.getEmail());
                updateIfPresent(existingMember::setPhone, member.getPhone());
                updateIfPresent(existingMember::setDepartment, member.getDepartment());
                updateIfPresent(existingMember::setMunicipality, member.getMunicipality());
                updateIfPresent(existingMember::setColony, member.getColony());
                updateIfPresent(existingMember::setIsCompaz, member.getIsCompaz());
                updateIfPresent(existingMember::setFechacumple, member.getFechacumple());
                updateIfPresent(existingMember::setPadre, member.getPadre());
                updateIfPresent(existingMember::setRelacion, member.getRelacion());

                return existingMember;
            })
            .map(memberRepository::save);
    }

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> findAll(Pageable pageable) {
        LOG.debug("Request to get all Members");
        return memberRepository.findAll(pageable);
    }

    /**
     * Get all the members with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Member> findAllWithEagerRelationships(Pageable pageable) {
        // 1. Si es Admin, sigue viendo todo con Eager Load
        if (SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            return memberRepository.findAllWithEagerRelationships(pageable);
        }

        // 2. Si es Líder, usamos tu nueva consulta filtrada (que ya tiene el fetch join)
        return SecurityUtils.getCurrentUserLogin()
            .map(login -> memberRepository.findAllByCreatedBy(pageable, login))
            .orElse(Page.empty(pageable));
    }

    /**
     * Get one member by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Member> findOne(Long id) {
        LOG.debug("Request to get Member : {}", id);
        return memberRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
    }

    private <T> void updateIfPresent(Consumer<T> setter, T value) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
