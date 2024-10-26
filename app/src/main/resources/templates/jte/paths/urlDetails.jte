@import DataTemplate.Paths.UrlPage
@import utils.NamedRoutes
@param UrlPage urlDetails
@template.layout.template(
content = @`

    <div>
        <h1>Сайт: ${urlDetails.getUrl().getAddress()}</h1>
        <table class="table table-bordered table-hover mt-3">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${urlDetails.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Адрес</td>
                <td>${urlDetails.getUrl().getAddress()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${String.valueOf(urlDetails.getUrl().getCreated())}</td>
            </tr>
            </tbody>
        </table>
        <hr>
        <a href="${NamedRoutes.root()}">На главную</a>  <a href="${NamedRoutes.urlList()}">К списку</a>
    </div>
`
)