@import hexlet.code.Url
@import hexlet.code.UrlCheck
@import java.util.List
@import java.time.format.DateTimeFormatter

@param Url url
@param List<UrlCheck> checks

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
<p><b>Дата добавления:</b> ${url.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}</p>

<h2 class="mt-4">Проверки сайта</h2>
<table class="table table-bordered">
    <thead>
    <tr>
        <th>ID</th>
        <th>Статус-код</th>
        <th>H1</th>
        <th>Title</th>
        <th>Description</th>
        <th>Дата проверки</th>
    </tr>
    </thead>
    <tbody>
    @for (UrlCheck check : checks)
        <tr>
            <td>${check.getId()}</td>
            <td>${check.getStatusCode()}</td>
            <td>${check.getH1()}</td>
            <td>${check.getTitle()}</td>
            <td>${check.getDescription()}</td>
            <td>${check.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}</td>
        </tr>
    @endfor
    </tbody>
</table>

<!-- Форма для новой проверки -->
<form action="/urls/${url.getId()}/checks" method="post" class="mt-3">
    <button type="submit" class="btn btn-primary">Проверить сайт</button>
</form>

<a href="/urls" class="btn btn-secondary mt-3">Назад к списку сайтов</a>

</body>
</html>
