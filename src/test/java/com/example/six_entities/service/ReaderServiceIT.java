package com.example.six_entities.service;

import com.example.six_entities.config.BaseIntegrationTest;
import com.example.six_entities.config.TestContainersConfiguration;
import com.example.six_entities.exception.ObjectNotFoundException;
import com.example.six_entities.model.Reader;
import com.example.six_entities.model.ReaderDto;
import com.example.six_entities.repository.ReaderRepository;
import com.example.six_entities.util.ReaderFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = TestContainersConfiguration.class)
class ReaderServiceIT extends BaseIntegrationTest {

    @Autowired
    ReaderService readerService;

    @Autowired
    ReaderRepository readerRepository;

    static final String CACHE = "readers";

    @AfterEach
    void tearDown() {
        readerRepository.deleteAll();
        cacheManager.getCache(CACHE).clear();
    }

    @Test
    void createReader_shouldCacheResult() {
        ReaderDto dto = ReaderFixture.createReaderDto();

        ReaderDto reader = readerService.createReader(dto);

        assertThat(cacheManager.getCache(CACHE).get(reader.getId())).isNotNull();
        assertThat(cacheManager.getCache(CACHE).get(reader.getId(), ReaderDto.class)).isEqualTo(reader);
    }

    @Test
    void getReaderById_shouldCacheResult() {
        Reader reader = readerRepository.save(ReaderFixture.createReaderForSave());

        ReaderDto response = readerService.getReaderById(reader.getId());

        assertThat(cacheManager.getCache(CACHE).get(reader.getId())).isNotNull();
        assertThat(cacheManager.getCache(CACHE).get(reader.getId(), ReaderDto.class)).isEqualTo(response);
    }

    @Test
    void getReaderById_shouldThrowObjectNotFoundException() {
        UUID notExistingId = UUID.randomUUID();

        assertThatThrownBy(() -> readerService.getReaderById(notExistingId))
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessageContaining("Reader not found")
                .hasMessageContaining(notExistingId.toString());
    }

    @Test
    void updateReader_shouldUpdateCache() {
        ReaderDto dto = ReaderFixture.createReaderDto();
        ReaderDto playerDto = readerService.createReader(dto);
        playerDto.setFirstName("change first name");

        readerService.updateReader(playerDto);

        assertThat(cacheManager.getCache(CACHE).get(playerDto.getId(), ReaderDto.class)).isEqualTo(playerDto);
    }

    @Test
    void deleteReader_shouldEvictCache() {
        ReaderDto dto = ReaderFixture.createReaderDto();
        ReaderDto playerDto = readerService.createReader(dto);

        readerService.deleteReader(playerDto.getId());

        assertThat(cacheManager.getCache(CACHE).get(playerDto.getId())).isNull();
        assertThat(readerRepository.findById(playerDto.getId())).isEmpty();
    }

}