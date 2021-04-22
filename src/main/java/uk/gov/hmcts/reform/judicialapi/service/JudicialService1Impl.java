
package uk.gov.hmcts.reform.judicialapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.judicialapi.domain.JudicialUserProfile;
import uk.gov.hmcts.reform.judicialapi.repository.JudicialUserProfileRepository;
import java.util.List;

@Slf4j
@Service
public class JudicialService1Impl {

    @Autowired
    JudicialUserProfileRepository judicialUserProfileRepository;

    @Autowired
    CacheManager cacheManager;

    @Cacheable(value = "autocomplete")
    public List<JudicialUserProfile> findAllJudicialUserProfiles() {
        log.info("::::: findAllJudicialUserProfiles called :::::");
        return judicialUserProfileRepository.findAllJudicialUserProfiles();
    }

    @Scheduled(cron = "0 0/3 *  * * ?")// every 3 min, we need to change to midnight 12
    public void evictAllCacheValues() {
        cacheManager.getCache("autocomplete").clear();
        log.info("::::: cache evicted::::: ");
    }
}

