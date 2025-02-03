@import hexlet.code.datatemplate.BasePage
@import hexlet.code.utils.NamedRoutes

@param  BasePage data

@template.layout.template(
    data = data,
    content = @`
        <div style="background-color: #14181c; color: #f0f2f4">
                <h2>Анализ страниц на наличие параметров для SE </h2>
                <hr>
                <form action="${hexlet.code.utils.NamedRoutes.urlList()}" method="post" class = "m-2 p-2">
                    <div>
                        <label for="urlInput" class="form-label">
                            Имя
                            <input class="form-control" placeholder="url" id="urlInput" aria-describedby="emailHelp" type="url" name="url"/>
                        </label>
                    </div>
                    <input type="submit" class="btn btn-primary" value="Добавить адрес страницы" />
                </form>
                <hr>
        </div>
            <a href="${NamedRoutes.urlList()}">Список страниц</a>
    `
)