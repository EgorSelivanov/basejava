package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private static final int STORAGE_LIMIT = 10000;
    private Resume[] storage = new Resume[STORAGE_LIMIT];
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        if (getIndexResume(r.getUuid()) != -1)
            System.out.println("Error! A resume with this id already exists!");
        else if (size >= STORAGE_LIMIT)
            System.out.println("Error! Storage overflow!");
        else {
            storage[size] = r;
            size++;
        }
    }

    public void update(Resume r) {
        int index = getIndexResume(r.getUuid());
        if (index == -1)
            System.out.println("Error! Resume with " + r.getUuid() + " uuid is not in the storage");
        else
            storage[index] = r;
    }

    public Resume get(String uuid) {
        int index = getIndexResume(uuid);
        if (index == -1) {
            System.out.println("Error! Resume with " + uuid + " uuid is not in the storage");
            return null;
        }
        return storage[index];
    }

    public void delete(String uuid) {
        int index = getIndexResume(uuid);
        if (index == -1) {
            System.out.println("Error! Resume with " + uuid + " uuid is not in the storage");
            return;
        }
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int getIndexResume(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }
}
