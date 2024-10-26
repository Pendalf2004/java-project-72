@import DataTemplate.Paths.ListPage
@import utils.NamedRoutes
@param  ListPage urlListData

@template.layout.template(
content = @`
    <h2>Список страниц</h2>
    <hr>
        ${urlListData.getMsg()}
        @if(urlListData != null)
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
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