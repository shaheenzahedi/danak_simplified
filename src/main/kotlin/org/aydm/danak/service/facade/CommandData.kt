package org.aydm.danak.service.facade

import java.util.function.Consumer

data class CommandData(
    val command: String,
    val onSuccess: Consumer<String>,
    val onFailure: Consumer<String>
)
