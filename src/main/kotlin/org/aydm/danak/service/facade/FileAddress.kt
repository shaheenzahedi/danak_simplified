package org.aydm.danak.service.facade

data class FileAddress(
    var version: Int,
    val name: String,
    val path: String,
    val checksum: String
){
    companion object{
        fun fromCSVLine(version: Int,line:String):FileAddress{
            val values = line.split(',')
            return FileAddress(
                version = version,
                name = values[0],
                path=values[1],
                checksum = values[2]
            )
        }
        fun fromDiffLine(version: Int,line:String):FileAddress{
            val values = line.substring(2).split(',')
            return FileAddress(
                version = version,
                name = values[0],
                path=values[1],
                checksum = values[2]
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileAddress

        if (checksum != other.checksum) return false
        if (name != other.name) return false
        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        var result = version
        result = 31 * result + name.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + checksum.hashCode()
        return result
    }

}
