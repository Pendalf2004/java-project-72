@import hexlet.code.datatemplate.paths.ListPage
@import hexlet.code.repository.UrlRepository
@import hexlet.code.utils.NamedRoutes
@param  ListPage urlListData

@template.layout.template(
content = @`
    <main>
    <h2>Список страниц</h2>
    <hr>
        ${urlListData.getMsg()}
        @if(urlListData != null)
            <table class="w-75 p-3">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Последняя проверка</th>
                    <th>Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @for(var url : urlListData.getUrlList())
                <tr>
                    <td>
                        ${url.getId()}
                    </td>
                    <td>
                        <a href="${NamedRoutes.urlPath(url.getId())}">${url.getAddress()}</a>
                    </td>
                    <td>
                        @if(url.getLastCheckTime() != null)
                            ${UrlRepository.getTime(url.getLastCheckTime())}
                        @endif
                    </td>
                    <td>
                        ${url.getStatusCode()}
                    </td>
                    @endfor
                </tr>
                </tbody>
            </table>
        @endif
    <hr>
    <a href="${NamedRoutes.root()}">На главную</a>
    </main>
`
)