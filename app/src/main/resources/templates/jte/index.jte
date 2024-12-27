@import hexlet.code.datatemplate.paths.RootPage
@import hexlet.code.utils.NamedRoutes
@param  RootPage data

@template.layout.template(
    content = @`
        @if(data != null && data.getMsg() != null)
           <div class="alert alert-primary" role="alert">
               <p>${data.getMsg()}</p>
           </div>
        @endif
        <main>
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
        </main>
    `
)