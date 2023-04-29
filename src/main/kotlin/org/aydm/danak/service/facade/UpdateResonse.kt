package org.aydm.danak.service.facade

data class UpdateResonse(
    val updates:List<FileAddress>,
    val deletes:List<FileAddress>
)
