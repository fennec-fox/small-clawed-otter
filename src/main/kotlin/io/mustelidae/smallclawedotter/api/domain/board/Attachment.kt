package io.mustelidae.smallclawedotter.api.domain.board

import io.mustelidae.smallclawedotter.api.common.Audit
import javax.persistence.CascadeType.DETACH
import javax.persistence.CascadeType.MERGE
import javax.persistence.CascadeType.PERSIST
import javax.persistence.CascadeType.REFRESH
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

/**
 * 첨부파일
 */
@Entity
class Attachment(
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    val type: Type,
    @Column(name = "attOrder")
    val order: Int,
    @Column(length = 1500)
    var path: String
) : Audit() {

    @Id
    @GeneratedValue
    var id: Long? = null
        protected set

    @Column(length = 1500)
    var thumbnail: String? = null

    enum class Type {
        FILE,
        IMAGE
    }

    // Attachment는 삭제를 하기 때문에 해당 Writing Entity가 삭제되지 않도록 REMOVE를 사용하지 않는다.
    @ManyToOne(cascade = [PERSIST, MERGE, DETACH, REFRESH], fetch = FetchType.LAZY)
    @JoinColumn(name = "writingId")
    var writing: Writing? = null
        protected set

    fun setBy(writing: Writing) {
        this.writing = writing
        if (writing.attachments.contains(this).not())
            writing.addBy(this)
    }
}
