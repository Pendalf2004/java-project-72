package datatemplate.paths;

import datatemplate.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.CheckModel;
import model.UrlModel;

import java.util.List;


@Getter
@AllArgsConstructor
public class UrlPage extends BasePage {
    private UrlModel url;
    private List<CheckModel> checks;
}
