package org.aydm.danak.web.rest

import org.aydm.danak.service.FileService
import org.aydm.danak.service.facade.AssetFacade
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.WebSocketSession
import java.io.File
import java.io.FileInputStream


@RestController
@RequestMapping("/api")
class FileResource(
    private val assetFacade: AssetFacade
) {

    @GetMapping("versionAssets")
    fun versionAsset(@RequestParam version:Int) {
        assetFacade.computeDiff(version);
    }

}
