<html>
<body>
<h2>Hello World!</h2>

<form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file">
    <input type="submit" value="上传">
</form>


<form action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file" accept="image/png,image/jpg">
    <input type="submit" value="上传">
</form>
</body>
</html>
