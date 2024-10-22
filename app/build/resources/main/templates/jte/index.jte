@template.layout.template(
    content = @`
        <div class="mx-auto p-4 py-md-5">
            <main>
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

            </main>
        </div>
    `
)