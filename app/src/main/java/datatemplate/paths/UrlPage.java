package datatemplate.paths;

import datatemplate.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.UrlModel;


@Getter
@AllArgsConstructor
public class UrlPage extends BasePage {
    private UrlModel url;
}
