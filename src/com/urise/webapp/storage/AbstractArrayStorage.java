package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        int index = getIndexResume(r.getUuid());
        if (index >= 0)
            System.out.println("Error! A resume with this id already exists!");
        else if (size >= STORAGE_LIMIT)
            System.out.println("Error! Storage overflow!");
        else {
            insertElement(r, index);
            size++;
        }
    }

    public void update(Resume r) {
        int index = getIndexResume(r.getUuid());
        if (index < 0)
            System.out.println("Error! Resume with " + r.getUuid() + " uuid is not in the storage");
        else
            storage[index] = r;
    }

    public void delete(String uuid) {
        int index = getIndexResume(uuid);
        if (index < 0) {
            System.out.println("Error! Resume with " + uuid + " uuid is not in the storage");
            return;
        }
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public Resume get(String uuid) {
        int index = getIndexResume(uuid);
        if (index < 0) {
            System.out.println("Error! Resume with " + uuid + " uuid is not in the storage");
            return null;
        }
        return storage[index];
    }

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertElement(Resume r, int index);

    protected abstract int getIndexResume(String uuid);
}
