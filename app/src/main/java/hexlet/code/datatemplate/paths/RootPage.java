package hexlet.code.datatemplate.paths;

import hexlet.code.datatemplate.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import hexlet.code.model.UrlModel;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class RootPage extends BasePage {
    private List<UrlModel> urls;
}
