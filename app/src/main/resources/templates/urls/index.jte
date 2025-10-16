@import hexlet.code.dto.UrlsPage
@import hexlet.code.util.DateUtils
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

@template.layout.page(
    page = page,
    content = @`
        <section>
            <div class="container-lg mt-5">
                <h1>Сайты</h1>
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var url : page.getUrls())
                        <tr>
                            <td>
                                ${url.getId()}
                            </td>
                            <td>
                                <a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a>
                            </td>
                            <td>
                                @if(page.getLastChecks().containsKey(url.getId()))
                                    ${DateUtils.getFormattedCheckDate(page.getLastChecks().get(url.getId()))}
                                @endif
                            </td>
                            <td>
                                @if(page.getLastChecks().containsKey(url.getId()))
                                    ${page.getLastChecks().get(url.getId()).getStatusCode()}
                                @endif
                            </td>

                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </section>
    `
)