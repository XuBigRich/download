# download
# 后段如何完美分割一个文件 并 使用Files.write 合并  （合并效率低）
    slice 包
# 后端如何多线程去合并文件  （合并高效率）
    random 包
    使用RandomAccessFile

# 后端断点续传   （使用redis 记录上传信息）
    记录整体文件复制时，的断点续传方案（与前端分片断点续传区分）
# 使用通道复制文件：
    channel 包
# redis 线程池的建立 （多线程操作同一个redis连接 报异常 会出现报错异常）
    java.net.SocketException: Broken pipe (Write failed)
    暂无要解决

# 网络接收 前端分割文件 
    network包

# 解决spring-boot不解析multipart/form-data 格式的请求  （application.yml配置文件）