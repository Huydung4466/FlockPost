package com.dunghsk.flockpost.common.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Base class cho mọi entity trong hệ thống.
 * Cung cấp id (UUID), createdAt, updatedAt tự động điền qua Spring Data JPA Auditing.
 *
 * Lưu ý: JpaAuditingConfig (@EnableJpaAuditing) phải được bật thì createdAt/updatedAt
 * mới tự động set - nếu quên bật, 2 field này sẽ luôn null.
 */

@Getter @Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * equals/hashCode dựa trên id, KHÔNG dùng Lombok @EqualsAndHashCode cho entity.
     * Lý do: Lombok generate equals/hashCode theo toàn bộ field, dễ vỡ khi Hibernate lazy-load
     * proxy hoặc khi entity chưa được persist (id null). Đây là pattern chuẩn cho JPA entity.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> thisClass = this instanceof org.hibernate.proxy.HibernateProxy
                ? ((org.hibernate.proxy.HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        Class<?> otherClass = o instanceof org.hibernate.proxy.HibernateProxy
                ? ((org.hibernate.proxy.HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        if (!thisClass.equals(otherClass)) return false;
        BaseEntity other = (BaseEntity) o;
        return id != null && Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
