@import datatemplate.paths.RootPage
@import utils.NamedRoutes
@param  RootPage data

@template.layout.template(
    content = @`
        @if(data != null && data.getMsg() != null)
            <p>${data.getMsg()}</p>
        @endif
        <main>
        <div class="mx-auto p-4 py-md-5">
                <h2>Hello world</h2>
                <hr>
                <form action="${utils.NamedRoutes.root()}" method="post">
                    <div>
                        <label>
                            Имя
                            <input type="text" name="url" />
                        </label>
                    </div>
                    <input type="submit" value="Send" />
                </form>
                <hr>
        </div>
            <a href="${NamedRoutes.urlList()}">Список страниц</a>
        </main>
    `
)