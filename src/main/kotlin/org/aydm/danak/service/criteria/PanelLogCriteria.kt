package org.aydm.danak.service.criteria

import org.aydm.danak.domain.enumeration.PanelLogType
import org.springdoc.api.annotations.ParameterObject
import tech.jhipster.service.Criteria
import tech.jhipster.service.filter.*
import java.io.Serializable

/**
 * Criteria class for the [org.aydm.danak.domain.PanelLog] entity. This class is used in
 * [org.aydm.danak.web.rest.PanelLogResource] to receive all the possible filtering options from the
 * Http GET request parameters.
 * For example the following could be a valid request:
 * ```/panel-logs?id.greaterThan=5&attr1.contains=something&attr2.specified=false```
 * As Spring is unable to properly convert the types, unless specific [Filter] class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
data class PanelLogCriteria(
    var id: LongFilter? = null,
    var createTimeStamp: InstantFilter? = null,
    var panelLogType: PanelLogTypeFilter? = null,
    var userId: LongFilter? = null,
    var distinct: Boolean? = null
) : Serializable, Criteria {

    constructor(other: PanelLogCriteria) :
        this(
            other.id?.copy(),
            other.createTimeStamp?.copy(),
            other.panelLogType?.copy(),
            other.userId?.copy(),
            other.distinct
        )

    /**
     * Class for filtering PanelLogType
     */
    class PanelLogTypeFilter : Filter<PanelLogType> {
        constructor()

        constructor(filter: PanelLogTypeFilter) : super(filter)

        override fun copy() = PanelLogTypeFilter(this)
    }

    override fun copy() = PanelLogCriteria(this)

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
