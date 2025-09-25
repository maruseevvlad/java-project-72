@import hexlet.code.Url
@import java.time.format.DateTimeFormatter
@param String name
@param java.util.List<Url> urls


<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Сохранение адреса</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">

<h1 class="mb-4">Сохранение адреса</h1>

<form action="/urls" method="post" class="mb-3">
    <div class="mb-3">
        <label for="urlInput" class="form-label">Введите адрес</label>
        <input type="text" class="form-control" id="urlInput" name="url" required>
    </div>
    <button type="submit" class="btn btn-primary">Сохранить</button>
</form>

<h2 class="mt-4">Сохранённые адреса</h2>
<ul class="list-group">
    @for (Url url : urls) {
    <li class="list-group-item">
        <a href="/urls/${url.getId()}">${url.getName()}</a>
        <span class="text-muted">(${url.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))})</span>
    </li>
    }
    @endfor
</ul>

</body>
</html>
