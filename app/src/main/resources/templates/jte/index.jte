@import hexlet.code.datatemplate.BasePage
@import hexlet.code.utils.NamedRoutes

@param  BasePage data

@template.layout.template(
    data = data,
    content = @`
        <div class="p-2 m-2">
                <h2>Hello world</h2>
                <hr>
                <form action="${hexlet.code.utils.NamedRoutes.urlList()}" method="post">
                    <div>
                        <label for="urlInput" class="form-label">
                            Имя
                            <input class="form-control" id="urlInput" aria-describedby="emailHelp" type="url" name="url" />
                        </label>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Добавить адрес страницы" />
                </form>
                <hr>
        </div>
            <a href="${NamedRoutes.urlList()}">Список страниц</a>
    `
)