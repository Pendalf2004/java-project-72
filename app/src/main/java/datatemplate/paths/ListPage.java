package datatemplate.paths;

import datatemplate.BasePage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.UrlModel;

import java.util.List;

@Getter
@AllArgsConstructor
public class ListPage extends BasePage {
    private List<UrlModel> urlList;
}
