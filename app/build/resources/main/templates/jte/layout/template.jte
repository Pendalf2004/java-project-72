@import gg.jte.Content
@import hexlet.code.datatemplate.BasePage
@import hexlet.code.utils.NamedRoutes

@param Content content
@param BasePage data = null

<!doctype html>
    <html lang="en">
        <head>
            <meta charset="utf-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
            <title>Hexlet forth project</title>
        </head>
        <body>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        @if(data != null && data.getMsg() != null)
            <div class="alert alert-primary" role="alert">
                <p>${data.getMsg()}</p>
            </div>
        @endif
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link active" href="${NamedRoutes.root()}">Главная</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${NamedRoutes.urlList()}">Список страниц</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
        <main>
            ${content}
        </main>
        </body>
    </html>