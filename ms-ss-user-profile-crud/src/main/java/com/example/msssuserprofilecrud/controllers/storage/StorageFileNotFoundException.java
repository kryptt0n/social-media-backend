package com.example.msssuserprofilecrud.controllers.storage;

import com.example.msssuserprofilecrud.controllers.storage.StorageException;

public class StorageFileNotFoundException extends StorageException {
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
