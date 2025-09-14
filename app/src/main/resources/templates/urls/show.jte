@import hexlet.code.Url
@param Url url

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${url.getName()}</title>
</head>
<body>
<h1>Информация о URL ${url.getName()}</h1>

<p><b>ID:</b> ${url.getId()}</p>
<p><b>Имя:</b> ${url.getName()}</p>
<p><b>Дата добавления:</b> ${url.getCreatedAt().toString()}</p>

</body>
</html>
