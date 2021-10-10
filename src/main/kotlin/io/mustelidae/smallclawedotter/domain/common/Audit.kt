package io.mustelidae.smallclawedotter.domain.common

import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
class Audit {

    @Audited
    @CreatedBy
    @LastModifiedBy
    @Column(length = 100)
    var auditor: String? = null

    @CreatedDate
    var createdAt: LocalDateTime? = null
        private set

    @Audited
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null
        private set
}
