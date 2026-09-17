package com.dunghsk.flockpost.common.exception;

/**
 * Dùng chung cho mọi "not found" thay vì tạo UserNotFoundException, CreatureNotFoundException...
 * riêng cho từng entity. Ví dụ dùng trong service:
 *
 *   userRepository.findById(id)
 *       .orElseThrow(() -> new ResourceNotFoundException("User", id));
 */

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(ErrorCode.RESOURCE_NOT_FOUND,
                "%s not found with id: %s".formatted(resourceName, identifier));
    }
}
