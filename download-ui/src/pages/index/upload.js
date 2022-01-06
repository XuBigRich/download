import md5 from 'js-md5' //引入MD5加密
import {
    postDate
} from '../../request/http.js'
import api from '../../request/api.js'

/**
 *
 * @param randoms
 * @param file 要上传的文件
 * @param pieceSize 每片多大
 * @param success 成功要执行的操作
 * @param error 失败要执行的操作
 */
export const uploadByPieces = ({file, pieceSize = 2, success, error}) => {
    // 如果文件传入为空直接 return 返回
    if (!file || !file.size) return
    let fileMD5 = ''// 总文件列表
    //每片大小
    const chunkSize = pieceSize * 1024 * 1024 // 5MB一片
    //总片数
    const chunkCount = Math.ceil(file.size / chunkSize) // 总片数
    // 获取md5
    const readFileMD5 = () => {
        // 读取视频文件的md5
        console.log("获取文件的MD5值")
        let fileRederInstance = new FileReader()
        //读取文件流
        fileRederInstance.readAsBinaryString(file)
        fileRederInstance.addEventListener('load', e => {
            let fileBolb = e.target.result
            fileMD5 = md5(fileBolb)
            console.log('fileMD5', fileMD5)
            console.log("文件未被上传，将分片上传")
            //
            readChunkMD5()
        })
    }
    //对文件进行分片
    const getChunkInfo = (file, currentChunk, chunkSize) => {
        //计算每片的开始
        let start = currentChunk * chunkSize
        //确保最终片不会大于 文件大小
        let end = Math.min(file.size, start + chunkSize)
        //将文件按照计算出来的 数据 进行分割，得到一个Blob对象
        let chunk = file.slice(start, end)
        //将Blob 转换为File对象,就是将Blob 添加一个[] 变为Blob数组， 如下[chunk]
        let sourceFileName = file.name.split(".")[0];
        let chunkFile = new File([chunk], sourceFileName + "_" + currentChunk + ".tmp")
        console.log(chunkFile)
        //返回一个对象，文件开始位置 与结束位置 与分片
        return {start, end, chunkFile}
    }
    // 针对每个文件进行chunk处理
    const readChunkMD5 = () => {
        // 针对单个文件进行chunk上传
        for (var i = 0; i < chunkCount; i++) {
            //得到分片对象
            const chunk = getChunkInfo(file, i, chunkSize)
            console.log("总片数" + chunkCount)
            console.log("分片后的数据---测试：" + i)
            console.log(chunk)
            //执行上传请求
            uploadChunk({chunk, currentChunk: i, chunkCount})
        }
    }
    // const FormData = require('form-data');
    //执行上传请求
    const uploadChunk = (chunkInfo) => {
        // 创建formData对象，下面是结合不同项目给后端传入的对象。
        let fetchForm = new FormData()
        fetchForm.append('chunkSize', chunkSize)
        fetchForm.append('file', chunkInfo.chunk.chunkFile)
        fetchForm.append('filename', file.name)
        fetchForm.append('totalChunks', chunkInfo.chunkCount)
        fetchForm.append('totalSize', file.length)
        fetchForm.append('md5', fileMD5)
        // let fetchForm = {
        //     'chunkNumber': chunkInfo.currentChunk + 1,
        //     'chunkSize': chunkSize,
        //     'currentChunkSize': chunkInfo.chunk.size,
        //     'file': chunkInfo.chunk,
        //     'filename': file.name,
        //     'totalChunks': chunkInfo.chunkCount,
        //     'totalSize': file.length,
        //     'md5': fileMD5
        // }

        postDate(api.upload, fetchForm).then(res => {
            console.log("分片上传返回信息：" + res)
            if (res.code == 200) {
                // 结合不同项目 将成功的信息返回出去，这里可变的是指 res.data[0]
                success(res.data[0])
                // 下面如果在项目中没有用到可以不用打开注释
                // if (chunkInfo.currentChunk < chunkInfo.chunkCount - 1) {
                //   console.log("分片上传成功")

                // } else {
                //   // 当总数大于等于分片个数的时候
                //   if ((chunkInfo.currentChunk + 1) == chunkInfo.chunkCount) {
                //     console.log("文件开始------合并成功")
                //     success(res.data[0])
                //   }
                // }
            } else {
                console.log(res.message)
            }
        }).catch((e) => {
            error && error(e)
        })

        // let fetchForm = {
        //     'chunkNumber': chunkInfo.currentChunk + 1,
        //     'chunkSize': chunkSize,
        //     'currentChunkSize': chunkInfo.chunk.size,
        //     'file': chunkInfo.chunk,
        //     'filename': file.name,
        //     'totalChunks': chunkInfo.chunkCount,
        //     'totalSize': file.length,
        //     'md5': fileMD5
        // }
        // console.log("嘿嘿 ")
        // postKnow(api.upload, fetchForm).then(res => {
        //     console.log("分片上传返回信息：" + res)
        //     if (res.code == 200) {
        //         // 结合不同项目 将成功的信息返回出去，这里可变的是指 res.data[0]
        //         success(res.data[0])
        //         // 下面如果在项目中没有用到可以不用打开注释
        //         // if (chunkInfo.currentChunk < chunkInfo.chunkCount - 1) {
        //         //   console.log("分片上传成功")
        //
        //         // } else {
        //         //   // 当总数大于等于分片个数的时候
        //         //   if ((chunkInfo.currentChunk + 1) == chunkInfo.chunkCount) {
        //         //     console.log("文件开始------合并成功")
        //         //     success(res.data[0])
        //         //   }
        //         // }
        //     }
        // }).catch((e) => {
        //     error && error(e)
        // })

    }
    readFileMD5() // 开始执行代码
}
