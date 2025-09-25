@import hexlet.code.Url
@param Url url

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${url.getName()}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">

<h1>Информация о URL</h1>

<p><b>ID:</b> ${url.getId()}</p>
<p><b>Имя:</b> ${url.getName()}</p>
<p><b>Дата добавления:</b> ${url.getCreatedAt().toString()}</p>

<a href="/" class="btn btn-secondary mt-3">Назад</a>

</body>
</html>
