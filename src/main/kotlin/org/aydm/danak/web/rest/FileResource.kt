package org.aydm.danak.web.rest

import org.aydm.danak.service.facade.AssetFacade
import org.aydm.danak.service.facade.FileAddress
import org.aydm.danak.service.facade.UpdateResonse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api")
class FileResource(
    private val assetFacade: AssetFacade
) {

    @GetMapping("version-assets")
    fun versionAsset(@RequestParam version:Int) {
        assetFacade.versionAsset(version);
    }
    @GetMapping("download-assets")
    fun downloadAssets(@RequestParam version: Int): List<FileAddress>? {
        return assetFacade.download(version);
    }
    @GetMapping("update-assets")
    fun versionAsset(@RequestParam fromVersion:Int,@RequestParam toVersion:Int): UpdateResonse? {
        return assetFacade.updateAssets(fromVersion,toVersion);
    }

}
