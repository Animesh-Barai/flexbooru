package onlymash.flexbooru.util

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import java.io.File
import java.net.URLDecoder
import java.util.*

fun Context.getUriFromSAFTree(path: String, fileName: String): Uri {
    val treeUri = Uri.parse(path)
    val docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri))
    return Uri.parse("$docUri${File.separator}$fileName")
}

fun Context.createUriFromSAFTree(path: String, fileName: String): Uri? {
    val treeUri = Uri.parse(path)
    val docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri))
    return DocumentsContract.createDocument(contentResolver, docUri, fileName.getMimeType(), fileName)
}

fun Context.deleteDocumentFromSAF(fileUri: Uri) {
    DocumentsContract.deleteDocument(contentResolver, fileUri)
}

fun Uri.toFile(): File? {
    var file: File? = null
    if (isSAFUri()) {
        val id = DocumentsContract.getDocumentId(this)
        file = getFileFromDocumentIdSAF(id, toString().fileName())!!
    } else {
        path?.let {
            file = File(it)
        }
    }
    return file
}

fun String.getFolderFromSAFTree(): File? {
    val treeUri = Uri.parse(this)
    val docUri = DocumentsContract.buildDocumentUriUsingTree(treeUri, DocumentsContract.getTreeDocumentId(treeUri))
    return getFileFromDocumentIdSAF(DocumentsContract.getTreeDocumentId(docUri), fileName())
}

fun Uri.isSAFUri() = "com.android.externalstorage.documents" == authority

private fun getFileFromDocumentIdSAF(id: String, fileName: String): File? {
    var file: File? = null

    val split = id.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

    if (split.size >= 2) {
        val type = split[0]
        val path = split[1]

        val storagePoints = File("/storage").listFiles()

        if ("primary".equals(type, ignoreCase = true)) {
            val externalStorage = Environment.getExternalStorageDirectory()
            file = File(externalStorage, path)
        }

        var i = 0

        while (storagePoints != null && i < storagePoints.size && file == null) {
            val externalFile = File(storagePoints[i], path)

            if (externalFile.exists()) {
                file = externalFile
            }

            i++
        }
    }

    val normalizedFileName = URLDecoder.decode(fileName, "utf-8").substringAfterLast(":")

    if (!file?.name.equals(normalizedFileName)) {
        file = File(file, normalizedFileName)
    }

    return file
}

fun String.getMimeType(): String {
    var extension = this.ext()
    // Convert the URI string to lower case to ensure compatibility with MimeTypeMap (see CB-2185).
    extension = extension.toLowerCase(Locale.getDefault())
    if (extension == "3ga") {
        return "audio/3gpp"
    } else if (extension == "js") {
        // Missing from the map :(.
        return "text/javascript"
    }
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""
}