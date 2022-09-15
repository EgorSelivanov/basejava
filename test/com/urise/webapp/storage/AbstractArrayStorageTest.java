package com.urise.webapp.storage;

import com.urise.webapp.exception.ExistStorageException;
import com.urise.webapp.exception.NotExistStorageException;
import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AbstractArrayStorageTest {
    private Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final Resume RESUME_1 = new Resume(UUID_1);

    private static final String UUID_2 = "uuid2";
    private static final Resume RESUME_2 = new Resume(UUID_2);

    private static final String UUID_3 = "uuid3";
    private static final Resume RESUME_3 = new Resume(UUID_3);

    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME_4 = new Resume(UUID_4);

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @BeforeEach
    public void setupThis() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    void size() {
        Assertions.assertEquals(3, storage.size());
    }

    @Test
    void clear() {
        storage.clear();
        Assertions.assertEquals(0, storage.size());
    }

    @Test
    void save() {
        storage.save(RESUME_4);
        Assertions.assertEquals(4, storage.size());
        Assertions.assertEquals(RESUME_4, storage.get(RESUME_4.getUuid()));
    }

    @Test
    void saveExist() {
        Assertions.assertThrows(ExistStorageException.class, () -> storage.save(RESUME_3));
    }

    @Test
    void saveOverflow() {
        try {
            for (int i = 4; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e){
            Assertions.fail("Exception occurred earlier than expected");
        }
        Assertions.assertThrows(StorageException.class, () -> storage.save(new Resume()), "Storage overflow");
    }

    @Test
    void update() {
        Resume resume = new Resume(UUID_3);
        storage.update(resume);
        Assertions.assertEquals(resume, storage.get(UUID_3));
    }

    @Test
    void updateNotExist() {
        Resume resume = new Resume("dummy");
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.update(resume));
    }

    @Test
    void delete() {
        storage.delete(UUID_3);
        Assertions.assertEquals(2, storage.size());
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get(UUID_3));
    }

    @Test
    void deleteNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.delete("dummy"));
    }

    @Test
    void getAll() {
        Resume[] array = storage.getAll();
        Assertions.assertEquals(3, array.length);
        Assertions.assertArrayEquals(new Resume[] {RESUME_1, RESUME_2, RESUME_3}, array);
    }

    @Test
    void get() {
        Assertions.assertEquals(RESUME_1, storage.get(UUID_1));
    }

    @Test
    void getNotExist() {
        Assertions.assertThrows(NotExistStorageException.class, () -> storage.get("dummy"));
    }
}